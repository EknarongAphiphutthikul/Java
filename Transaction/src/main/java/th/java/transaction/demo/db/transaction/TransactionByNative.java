package th.java.transaction.demo.db.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.java.transaction.demo.db.entity.AddressEntity;
import th.java.transaction.demo.db.entity.UserEntity;

@Component
public class TransactionByNative {
	
	@Autowired
	private DataSource dataSource;
	
	String sqlInsertUser = "insert into demo_user (user_id, user_name, updateDtm) value(?,?,?)";
	String sqlInsertAddress = "insert into demo_address (address_id, address_detail) value (?,?)";
//	String sqlUpdateUser = "update demo_user set user_name = ?, updateDtm = ? where user_id = ?";
	String sqlUpdateUser = "update demo_user set user_name = ?, updateDtm = ? where user_id = "
			+ "(select user_id from demo_user where user_id = ? and updateDtm < ?)";
	String sqlUpdateAddress = "update demo_address set address_detail = ? where address_id = ?";
	String select = "select count(*) from demo_user where user_id = ?";
	
	public int selectCount(int id) throws Exception {
		Connection con = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		try {
			Integer result = 0;
			con = dataSource.getConnection();
			con.setAutoCommit(true);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			preStm = con.prepareStatement(select);
			preStm.setLong(1, id);
			
			rs = preStm.executeQuery();

            while (rs.next()) {
            	result = rs.getInt(1);
            }
            
            return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != rs) {
		        try {
		            rs.close();
		        } catch (Exception e) {}
			}
			if (null != preStm) {
				try {
					preStm.close();
				} catch (Exception e) {}
			}
			if (null != con) {
				try {
					con.close();
				} catch (Exception e) {}
			}
		}
	}
	
	public void save(UserEntity userEntity, AddressEntity addressEntity) throws Exception {
		Connection con = null;
		PreparedStatement insertUserStatement = null;
		PreparedStatement insertAddressStatement = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			insertUserStatement = con.prepareStatement(sqlInsertUser);
			insertUserStatement.setInt(1, userEntity.getId());
			insertUserStatement.setString(2, userEntity.getName());
			insertUserStatement.setTimestamp(3, new Timestamp(userEntity.getUpdateDtm().getTime()));
			int countUser = insertUserStatement.executeUpdate();
			int countAddress = 0;
			if (countUser == 1) {
				insertAddressStatement = con.prepareStatement(sqlInsertAddress);
				insertAddressStatement.setInt(1, addressEntity.getId());
				insertAddressStatement.setString(2, addressEntity.getDetail());		
				countAddress = insertAddressStatement.executeUpdate();
			}
			if ((countUser + countAddress) == 2) {
				con.commit();
			} else {
				con.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (null != con) {
				try {
					con.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			throw e;
		} finally {
			if (null != insertUserStatement) {
				try {
					insertUserStatement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != insertAddressStatement) {
				try {
					insertAddressStatement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != con) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void update(UserEntity userEntity, AddressEntity addressEntity) throws Exception {
		Connection con = null;
		PreparedStatement updateUserStatement = null;
		PreparedStatement updateAddressStatement = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			updateUserStatement = con.prepareStatement(sqlUpdateUser);
			updateUserStatement.setString(1, userEntity.getName());
			updateUserStatement.setTimestamp(2, new Timestamp(userEntity.getUpdateDtm().getTime()));
			updateUserStatement.setInt(3, userEntity.getId());
			updateUserStatement.setTimestamp(4, new Timestamp(userEntity.getUpdateDtm().getTime()));
			int countUser = updateUserStatement.executeUpdate();
			int countAddress = 0;
			if (countUser == 1) {
				updateAddressStatement = con.prepareStatement(sqlUpdateAddress);
				updateAddressStatement.setString(1, addressEntity.getDetail());		
				updateAddressStatement.setInt(2, addressEntity.getId());
				countAddress = updateAddressStatement.executeUpdate();
			}
			if ((countUser + countAddress) == 2) {
				con.commit();
			} else {
				con.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (null != con) {
				try {
					con.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			throw e;
		} finally {
			if (null != updateUserStatement) {
				try {
					updateUserStatement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != updateAddressStatement) {
				try {
					updateAddressStatement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != con) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
