package me.stqlth.krypto.messages.debug;

import me.stqlth.krypto.utils.Logger;

import java.sql.SQLException;

public class DebugMessages {

    public void sqlDebug(SQLException ex) {
        Logger.Error("A SQL error was encountered.", ex);
    }

}
