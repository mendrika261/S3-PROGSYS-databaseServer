package server;

import interpreter.Query;

import java.io.DataOutputStream;
import java.io.IOException;

public class UserCommit extends Thread {
    DataOutputStream dataOutputStream;
    UserSession userSession;
    UserListener userListener;

    public UserCommit(DataOutputStream dataOutputStream, UserSession userSession, UserListener userListener) {
        setDataOutputStream(dataOutputStream);
        setUserSession(userSession);
        setUserListener(userListener);
    }

    @Override
    public void run() {
        try {
            while(getUserSession().getCommitOrder() != getUserListener().getCurrentCommitRank(getUserSession())) sleep(100);
            while(getUserListener().isCommittingState()) sleep(100);
            getUserListener().setCommittingState(true);
            getUserSession().getDatabase().commit();
            getUserSession().getHistory().clear();
            getUserSession().setCommitOrder(0);
            actualiseAllUser();
            getUserListener().setCommittingState(false);
        } catch (Exception ignored) {}
    }

    public void actualiseAllUser() {
        for(UserSession userSession: getUserListener().getUsers()) {
            try {
                userSession.getDatabase().loadDatabase();
                Query query = new Query(userSession.getDatabase());
                for (String req:userSession.getHistory())
                    query.resolve(req);
            } catch (Exception ignored) {}
        }
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public void setDataOutputStream(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public UserListener getUserListener() {
        return userListener;
    }

    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }
}
