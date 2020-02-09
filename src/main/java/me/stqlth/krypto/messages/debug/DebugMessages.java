package me.stqlth.krypto.messages.debug;

import me.stqlth.krypto.utils.Logger;

import java.sql.SQLException;

public class DebugMessages {

    public void sqlDebug(SQLException ex) {
        System.out.println("SQLExpection: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
//        Logger.Error("A SQL error was encountered.", ex);
    }

}
