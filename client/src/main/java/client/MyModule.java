package client;

import client.scenes.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    /**
     * Configuration method for the binder for the modules.
     *
     * @param binder - provided a specific binder object.
     */
    @Override
    public void configure(Binder binder) {
        binder.bind(GameCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SplashScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(QuestionCtrl.class).in(Scopes.SINGLETON);
        binder.bind(LeaderBoardCtrl.class).in(Scopes.SINGLETON);
    }
}