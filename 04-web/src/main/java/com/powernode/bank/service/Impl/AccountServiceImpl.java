package com.powernode.bank.service.Impl;


import com.powernode.bank.dao.AccountDao;
import com.powernode.bank.dao.Impl.AccountDaoImpl;
import com.powernode.bank.exceptions.MoneyNotEnoughException;
import com.powernode.bank.exceptions.TransferException;
import com.powernode.bank.pojo.Account;
import com.powernode.bank.service.AccountService;
import com.powernode.bank.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao = new AccountDaoImpl();

    @Override
    public void transfer(String fromActno, String toActno, double money) throws MoneyNotEnoughException, TransferException {

        //添加事务控制代码
        SqlSession sqlSession = SqlSessionUtil.openSession();

        //执行业务
        // 1.判断转出账户的余额是否充足
        Account fromAct = accountDao.selectByActno(fromActno);
        if (fromAct.getBalance() < money) {
            // 2.如果转出账户余额不足，提示用户
            throw new MoneyNotEnoughException("对不起，您的余额不足。");
        }

            // 3.如果转出账户余额充足，更新转出账户余额
            Account toAct = accountDao.selectByActno(toActno);
            fromAct.setBalance(fromAct.getBalance() - money);
            toAct.setBalance(toAct.getBalance() + money);
            // 更新数据库
            int count = accountDao.update(fromAct);
            //模拟异常
            String s=null;
            s.toString();

            // 4.更新转入账户余额
            count += accountDao.update(toAct);
            if (count != 2) {
                throw new TransferException("转账异常， 未知原因" );
            }else {
                System.out.println("转账成功");
            }

        //提交事务
        sqlSession.commit() ;
        //关闭事务
        //sqlSession.close();
        SqlSessionUtil.close(sqlSession);


    }
}
