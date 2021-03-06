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

import co.louiscap.entwasums.ents.Administrator;
import co.louiscap.entwasums.ents.properties.AccessLevel;
import co.louiscap.entwasums.pers.AdministratorFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Runs on application startup to ensure that an administrator user exists
 * @author Louis Capitanchik
 */

@Startup
@Singleton
public class StartupBean {
    
    @EJB
    private AdministratorFacade am;
    
    @PostConstruct
    public void init() {
        System.out.println("Running Admin Existence Routine");
        Administrator admin = am.findByUsername("admin");
        if(admin == null) {
            System.out.println("Creating New Admin User");
            admin = new Administrator();
            admin.setAccepted(true);
            admin.setAccess(AccessLevel.ADMIN);
            admin.setPassword("global");
            admin.setUsername("admin");
            am.create(admin);
        } else {
            System.out.println(admin);
            System.out.println(admin.getUsername());
        }
    }
    
}
