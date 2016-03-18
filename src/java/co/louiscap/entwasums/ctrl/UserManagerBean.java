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

import co.louiscap.entwasums.ents.properties.AccessLevel;
import co.louiscap.entwasums.ents.Interactor;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Louis Capitanchik
 */
@Named(value = "userManagerBean")
@SessionScoped
public class UserManagerBean implements Serializable {

    private static final long serialVersionUID = -2332705795830173905L;

    private Interactor sessionUser = null;
    
    /**
     * Creates a new instance of UserManagerBean
     */
    public UserManagerBean() {}
    
    public void setUser(Interactor user) {
        this.sessionUser = user;
    }
    public Interactor getUser() {
        return sessionUser;
    }
    public boolean isLoggedIn() {
        return sessionUser != null;
    }
    public AccessLevel getAccessLevel() {
        return sessionUser != null ?
                sessionUser.getAccess() :
                null;
    }
    public void logout() {
        this.setUser(null);
    }
    public boolean isAccessLevel(AccessLevel level) {
        return sessionUser.getAccess().equals(level);
    }
    public String doAuthRedirect(String viewId) {
        System.out.println(viewId);
        return "/index";
    }
}
