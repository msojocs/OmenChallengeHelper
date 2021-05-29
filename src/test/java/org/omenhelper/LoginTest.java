package org.omenhelper;

import org.junit.Test;
import org.omenhelper.Omen.Login;

/**
 * @Author jiyec
 * @Date 2021/5/29 14:17
 * @Version 1.0
 **/
public class LoginTest {
    @Test
    public void testLogin(){
        String email = System.getenv("EMAIL");
        String pass = System.getenv("PASS");
        Login login = new Login(email, pass);
        login.doIt();
    }
}
