package pe.edu.upeu.dto;

import lombok.Data;

@Data
public class SessionManager {
    static SessionManager instance;
    String userId;  // ← cambiar de Long a String
    String userName;
    String userPerfil;

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
}