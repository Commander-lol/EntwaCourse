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
import co.louiscap.entwasums.bus.exceptions.AuthenticationException;
import co.louiscap.entwasums.bus.exceptions.UsernameExistsException;
import co.louiscap.entwasums.ents.Interactor;
import co.louiscap.entwasums.ents.properties.AccessLevel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Louis Capitanchik
 */
@Named(value = "loginBean")
@ViewScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = -4123423684126981494L;

    @NotNull(message = "Username cannot be blank")
    private String username;
    
    @NotNull(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    private String passwordConf;
    private AccessLevel access;
    private boolean newUser;
    
    @EJB
    protected LoginService ls;
    
    private List<AccessLevel> validAccess;
    
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        validAccess = new ArrayList<>();
        validAccess.add(AccessLevel.STUDENT);
        validAccess.add(AccessLevel.STAFF);
        validAccess.add(AccessLevel.ORGANISATION);
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
     * Get the value of access
     *
     * @return the value of access
     */
    public AccessLevel getAccess() {
        return access;
    }

    /**
     * Set the value of access
     *
     * @param access new value of access
     */
    public void setAccess(AccessLevel access) {
        this.access = access;
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
    
    /**
     * Get the value of validAccess
     * 
     * @return the value of validAccess
     */
    public List<AccessLevel> getValidAccess() {
        return validAccess;
    }

    /**
     * Set the value of validAccess
     * 
     * @param validAccess new value of validAccess
     */
    public void setValidAccess(List<AccessLevel> validAccess) {
        this.validAccess = validAccess;
    }
    
    
    public String attemptLogin() {
        System.out.println("ATTEMPTING LOGIN");
        if (newUser) {
            System.out.println("NEW USER");
            return doSignup();
        } else {
            System.out.println("EXS USER");
            return doLogin();
        }
    }

    public String getActionType() {
        return isNewUser() ? "Register" : "Sign In";
    }
    
    private String doLogin() {
        try {
            Interactor i = ls.login(username, password);
            String controlPanel = "/" + i.getAccess().name + "/index";
            System.out.println(controlPanel);
            return controlPanel;
        } catch (AuthenticationException ex) {
            String message = ex.getMessage();
            ex.printStackTrace(System.err);
            FacesContext.getCurrentInstance().addMessage("loginErrors", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
            return null;
        }
    }

    private String doSignup() {
        try {
            ls.registerNewUser(username, password, access);
        } catch (UsernameExistsException ex) {
            String message = ex.getMessage();
            ex.printStackTrace(System.err);
            FacesContext.getCurrentInstance().addMessage("loginErrors", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
            return null;
        }
        System.out.println("COMPLETE REGISTER");
        return "/pending";
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
