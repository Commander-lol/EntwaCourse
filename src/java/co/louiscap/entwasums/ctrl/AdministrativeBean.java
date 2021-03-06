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

import co.louiscap.entwasums.bus.UserManagerService;
import co.louiscap.entwasums.bus.exceptions.AuthorisationException;
import co.louiscap.entwasums.ents.Interactor;
import co.louiscap.entwasums.ents.PendingUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Louis Capitanchik
 */
@Named(value = "administrativeBean")
@ViewScoped
public class AdministrativeBean implements Serializable{

    private static final long serialVersionUID = 1784771828717971738L;

    @EJB
    UserManagerService ums;
    
//    List<PendingUser> pendingUsers;
    
    /**
     * Creates a new instance of AdministrativeBean
     */
    public AdministrativeBean() {
//        pendingUsers = ums.getPendingUsers();
    }
    
    public List<PendingUser> getPendingUsers() {
        try {
            return ums.getPendingUsers();
        } catch (AuthorisationException ex) {
            String message = ex.getMessage();
            ex.printStackTrace(System.err);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
            return null;
        }
    }
    
    public List<Interactor> getUsers() {
        try {
            return ums.getAllUsers();
        } catch (AuthorisationException ex) {
            String message = ex.getMessage();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
        }
        return new ArrayList<>();
    }
    
    public String acceptUser(PendingUser pu) {
        System.out.println("Accepted " + pu.getUsername());
        try {
            ums.createUserFromPending(pu);
        } catch (AuthorisationException ex) {
            String message = ex.getMessage();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
        }
        return null;
    }
    
    public String rejectUser(PendingUser pu) {
        ums.removePendingUser(pu);
        return null;
    }
    
    public String deleteUser(Interactor i) {
        ums.deleteUserAccount(i);
        return null;
    }
}
