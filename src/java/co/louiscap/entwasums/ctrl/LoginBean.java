/*
Copyright (c) 2015, Louis Capitanchik
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of EntwaSums nor the names of its associated properties or
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package co.louiscap.entwasums.ctrl;

import co.louiscap.entwasums.bus.LoginService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Louis Capitanchik
 */
@Named(value = "loginBean")
@RequestScoped
public class LoginBean {

    @NotNull(message = "Username cannot be blank")
    private String username;

    @NotNull(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String passwordConf;

    private String email;

    private boolean newUser;
    
    @Inject
    protected UserManagerBean umb;
    
    @EJB
    protected LoginService ls;

    
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        newUser = false;
    }
    
    /**
     * Get the value of passwordConf
     *
     * @return the value of passwordConf
     */
    public String getPasswordConf() {
        return passwordConf;
    }

    /**
     * Set the value of passwordConf
     *
     * @param passwordConf new value of passwordConf
     */
    public void setPasswordConf(String passwordConf) {
        this.passwordConf = passwordConf;
    }

    /**
     * Get the value of newUser
     *
     * @return the value of newUser
     */
    public boolean isNewUser() {
        return newUser;
    }

    /**
     * Set the value of newUser
     *
     * @param newUser new value of newUser
     */
    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    /**
     * Get the value of email
     *
     * @return the value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the value of email
     *
     * @param email new value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String attemptLogin() {
        if (newUser) {
            return doSignup();
        } else {
            return doLogin();
        }
    }

    public String getButtonText() {
        return isNewUser() ? "Register" : "Sign In";
    }
    
    private String doLogin() {
        return null;
    }

    private String doSignup() {
        if(getPassword().equals(getPasswordConf())) {
            //
        } return null;
    }
    
    public void validateUsername(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(isNewUser()) {
            if(!ls.checkIfUsernameAvailable((String) value)) {
                String message = "That username is already in use";
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
            }
        }
    }
}
