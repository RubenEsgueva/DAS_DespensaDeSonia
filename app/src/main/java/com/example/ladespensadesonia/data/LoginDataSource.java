package com.example.ladespensadesonia.data;

import android.content.Context;

import com.example.ladespensadesonia.data.model.LoggedInUser;
import com.example.ladespensadesonia.DB.DBHelper;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private DBHelper db;

    public Result<LoggedInUser> login(Context context, String username, String password) {

        try {
            //Comprobar usuario correcto
            db = new DBHelper(context);
            boolean loginCorrecto = db.checkLogin(username, password);

            if(loginCorrecto) {
                LoggedInUser currUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username);
                return new Result.Success<>(currUser);
            } else {
                return new Result.Error(new IOException("Usuario o contraseña incorrectos."));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error al iniciar sesión", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}