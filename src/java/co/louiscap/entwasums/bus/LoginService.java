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
package co.louiscap.entwasums.bus;

import co.louiscap.entwasums.bus.exceptions.AuthenticationException;
import co.louiscap.entwasums.bus.exceptions.UsernameExistsException;
import co.louiscap.entwasums.ctrl.UserManagerBean;
import co.louiscap.entwasums.ents.Interactor;
import co.louiscap.entwasums.ents.PendingUser;
import co.louiscap.entwasums.ents.properties.AccessLevel;
import co.louiscap.entwasums.pers.InteractorFacade;
import co.louiscap.entwasums.pers.PendingUserFacade;
import co.louiscap.utils.Encrypt;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author Louis Capitanchik
 */
@Stateless
public class LoginService {
    
    private static final String DEFAULT_AUTH_ERROR = "Invalid Username or Password";
    
    @EJB
    private InteractorFacade inf;
    @EJB
    private PendingUserFacade puf;
    
    @Inject
    private UserManagerBean umb;
    
    public Interactor login(String username, String password) throws AuthenticationException {
        Interactor i = inf.getByUsername(username);
        if(i == null) {
            throw new AuthenticationException(false, DEFAULT_AUTH_ERROR);
        } else {
            if(Encrypt.VerifySHA256Hex(password, i.getPassword())) {
                umb.setUser(i);
                return i;
            } else {
                throw new AuthenticationException(true, DEFAULT_AUTH_ERROR);
            }
        }
    }
    
    public PendingUser loginAsPendingUser(String username, String password) throws AuthenticationException {
        PendingUser pu = puf.getByUsername(username);
        if(pu == null) {
            throw new AuthenticationException(false, DEFAULT_AUTH_ERROR);
        } else {
            if(Encrypt.VerifySHA256Hex(password, pu.getPassword())) {
                return pu;
            } else {
                throw new AuthenticationException(true, DEFAULT_AUTH_ERROR);
            }
        }
    }
    
    public void logout() {
        umb.logout();
    }
    
    public boolean checkIfUsernameAvailable (String username) {
        Interactor asUser = inf.getByUsername(username);
        PendingUser asPU = puf.getByUsername(username);
        return asUser == null && asPU == null;
    }
    
    public void registerNewUser(String username, String password, AccessLevel access) throws UsernameExistsException {
        PendingUser pu = new PendingUser();
        if(checkIfUsernameAvailable(username)) {
            pu.setUsername(username);
            pu.setPassword(password);
            pu.setRequestedAccess(access);
            puf.create(pu);
        } else {
            throw new UsernameExistsException("The username " + username + " already exists");
        }
        
    }
}
