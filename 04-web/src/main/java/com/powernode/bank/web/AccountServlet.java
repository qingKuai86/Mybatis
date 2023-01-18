package com.powernode.bank.web;

import com.powernode.bank.exceptions.MoneyNotEnoughException;
import com.powernode.bank.exceptions.TransferException;
import com.powernode.bank.service.AccountService;
import com.powernode.bank.service.Impl.AccountServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/transfer")
public class AccountServlet extends HttpServlet {

    private AccountService accountService = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        //获取表单数据
        String fromActno = request.getParameter( "fromActno");
        String toActno = request.getParameter( "toActno");
        double money = Double.parseDouble(request.getParameter("money"));
        //调用service的转账 方法完成转账。(调业 务层)
        try {
            //调用service的转账方法完成转账。(调业务层)
            accountService. transfer(fromActno, toActno, money);
            //程序能够走到这里，表示转账一定成功了。
            // 调用View完成展示结果。
            resp.sendRedirect( request.getContextPath() + "/success.html");
        } catch (MoneyNotEnoughException e) {
            resp.sendRedirect( request.getContextPath() + "/error1.html");
        } catch (TransferException e) {
            resp.sendRedirect( request.getContextPath() + "/error2.html");
        } catch (Exception e) {
            resp.sendRedirect( request.getContextPath() + "/error2.html");
        }

    }
}
