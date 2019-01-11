package client.core;

import client.authenticator.AuthData;
import client.authenticator.EmailAuthenticator;
import client.core.common.SendedMessage;
import client.core.common.Sender;
import client.core.exceptions.NoInternetException;
import client.core.interfaces.IReceiver;
import client.core.interfaces.ISender;
import client.core.interfaces.callbacks.LoginCallback;
import client.core.interfaces.callbacks.MessageErrorCallback;
import client.core.interfaces.callbacks.SuccessCallback;
import client.utils.LoginChecker;
import com.sun.istack.internal.NotNull;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;


public class BaseGmailClient extends LoginRequiredClient implements MailAPI {
    public BaseGmailClient() {
    }

    public BaseGmailClient(@NotNull EmailAuthenticator authenticator) {
        setAuthenticator(authenticator);
    }

    @Override
    public void auth(AuthData authData) {
        final LoginCallback<LoginRequiredClient, MessagingException> callbacks = getLoginCallbacks();
        callIfNotNull(callbacks, callbacks::beforeLogin);
        try {
            LoginChecker.check(getAuthenticator());
            callIfNotNull(callbacks, () -> callbacks.onSuccessLogin(this));
            getAuthenticator().setDataCorrect(true);
        } catch (NoSuchProviderException | NoInternetException | AuthenticationFailedException e) {
            callIfNotNull(callbacks, () -> callbacks.onLoginError(e));
            getAuthenticator().setDataCorrect(false);
        }
    }

    @Override
    public void auth(AuthData authData, AuthCallback callback) {
        callIfNotNull(callback, () -> getBeforeLoginCallback().call());
        try {
            LoginChecker.check(getAuthenticator());
            callIfNotNull(callback, callback::onSuccess);
            getAuthenticator().setDataCorrect(true);
        } catch (NoSuchProviderException | NoInternetException | AuthenticationFailedException e) {
            callIfNotNull(callback, () -> callback.onError(e));
            getAuthenticator().setDataCorrect(false);
        }
    }

    public <T extends BaseGmailClient> T auth() {
        return thisReference(() -> auth(getAuthData()));
    }

    public <T extends BaseGmailClient> T auth(AuthCallback callback) {
        return thisReference(() -> auth(getAuthData(), callback));
    }

    @Override
    public void send(SendedMessage message) {
        final Sender sender = Sender.getInstance(getAuthenticator());
        try {
            if (getAuthenticator().isDataCorrect())
                sender.send(message);
        } catch (NoSuchProviderException | NoInternetException | SendFailedException e) {
            // TODO: 11.01.19
            e.printStackTrace();
        }
    }

    public void send(SendedMessage message, ISender.SendCallback callback) {
        final Sender sender = Sender.getInstance(getAuthenticator());
        try {
            if (getAuthenticator().isDataCorrect()) {
                sender.send(message);
                callIfNotNull(callback, callback::onSuccess);
            }
        } catch (NoSuchProviderException | NoInternetException | SendFailedException e) {
            callback.onError(e);
        }
    }

    public void send(SendedMessage message, SuccessCallback successCallback, MessageErrorCallback errorCallback) {
        send(message, new SendCallback() {
            @Override public void onSuccess() { successCallback.onSuccess(); }
            @Override public void onError(MessagingException e) { errorCallback.onError(e); }
        });
    }

    @Override
    public void receive(IReceiver.ReceiveCallback callback) {
        // TODO: 11.01.19
    }
}