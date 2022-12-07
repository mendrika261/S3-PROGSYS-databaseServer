package server;

public class CommitThread extends Thread {
    UserListener userListener;

    public CommitThread(UserListener userListener) {
        setUserListener(userListener);
    }

    @Override
    public void run() {
        for (UserSession userSession:getUserListener().getUsers()) {
            try {
                userSession.getDatabase().loadDatabase();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public UserListener getUserListener() {
        return userListener;
    }

    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }
}
