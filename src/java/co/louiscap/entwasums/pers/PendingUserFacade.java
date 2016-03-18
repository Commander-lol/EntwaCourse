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
package co.louiscap.entwasums.pers;

import co.louiscap.entwasums.ents.PendingUser;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Louis Capitanchik
 */
@Stateless
public class PendingUserFacade extends AbstractFacade<PendingUser> {
    
    @PersistenceContext(unitName = "EntwaSums696771PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PendingUserFacade() {
        super(PendingUser.class);
    }

    public PendingUser getByUsername(String username) {
        Query q = em.createQuery("SELECT pu FROM PendingUser pu WHERE pu.username = :username");
        q.setParameter("username", username);
        PendingUser pu;
        try {
            pu = (PendingUser) q.getSingleResult();
        } catch(NoResultException nre) {
            pu = null;
        }
        return pu;
    }
    
    public List<PendingUser> getAllPendingUsers() {
        Query q = em.createQuery("SELECT pu FROM PendingUser pu WHERE 1=1");
        List<PendingUser> pus;
        try {
            pus = (List<PendingUser>) q.getResultList();
        } catch(NoResultException nre) {
            pus = new ArrayList<>();
        }
        return pus;
    }
    
}
