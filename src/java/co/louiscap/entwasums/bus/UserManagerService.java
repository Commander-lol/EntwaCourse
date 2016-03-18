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

import co.louiscap.entwasums.bus.exceptions.AuthorisationException;
import co.louiscap.entwasums.ctrl.UserManagerBean;
import co.louiscap.entwasums.ents.Interactor;
import co.louiscap.entwasums.ents.Organisation;
import co.louiscap.entwasums.ents.PendingUser;
import co.louiscap.entwasums.ents.Staff;
import co.louiscap.entwasums.ents.Student;
import co.louiscap.entwasums.pers.InteractorFacade;
import co.louiscap.entwasums.pers.OrganisationFacade;
import co.louiscap.entwasums.pers.PendingUserFacade;
import co.louiscap.entwasums.pers.StaffFacade;
import co.louiscap.entwasums.pers.StudentFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author Louis Capitanchik
 */
@Stateless
public class UserManagerService {
    @Inject
    UserManagerBean umb;
    @EJB
    InteractorFacade inf;
    @EJB
    StudentFacade studentf;
    @EJB
    StaffFacade stafff;
    @EJB
    OrganisationFacade of;
    @EJB
    PendingUserFacade puf;
    
    public List<PendingUser> getPendingUsers() throws AuthorisationException {
//        if(!umb.isAccessLevel(AccessLevel.ADMIN)){
//            throw new AuthorisationException();
//        }
        return puf.getAllPendingUsers();
    }
    
    public List<Interactor> getAllUsers() throws AuthorisationException {
        return inf.getAll();
    }
    
    public void createUserFromPending(PendingUser pu) throws AuthorisationException {
//        if(!umb.isAccessLevel(AccessLevel.ADMIN)) {
//            throw new AuthorisationException();
//        }
        switch(pu.getRequestedAccess()) {
            case STUDENT:
                Student stu = new Student();
                stu.setUsername(pu.getUsername());
                stu.setPassword(pu.getPassword());
                stu.setPreEncrypt(true);
                studentf.create(stu);
                break;
            case STAFF:
                Staff sta =  new Staff();
                sta.setUsername(pu.getUsername());
                sta.setPassword(pu.getPassword());
                sta.setPreEncrypt(true);
                stafff.create(sta);
                break;
            case ORGANISATION:
                Organisation org = new Organisation();
                org.setUsername(pu.getUsername());
                org.setPassword(pu.getPassword());
                org.setPreEncrypt(true);
                of.create(org);
                break;
            default:
                throw new AuthorisationException();
        }
        puf.remove(pu);
    }
    
    public void removePendingUser(PendingUser pu) {
        puf.remove(pu);
    }
    
    public void deleteUserAccount(Interactor i) {
        inf.remove(i);
    }
}
