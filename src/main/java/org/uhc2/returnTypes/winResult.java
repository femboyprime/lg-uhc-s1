package org.uhc2.returnTypes;

import org.uhc2.enums.camps;

public class winResult {
    private boolean _isWin;
    private String _winMessage;
    private camps _winCamp;

    // public "set"
    public winResult(boolean isWin, String winMessage, camps winCamp) {
        this._isWin = isWin;
        this._winMessage = winMessage;
        this._winCamp = winCamp;
    }

    // "get"
    public boolean getWin() {
        return _isWin;
    }

    public String getMessage() {
        return _winMessage;
    }

    public camps getCamp() {
        return _winCamp;
    }

    // duplicates
    public String getWinMessage() {
        return getMessage();
    }

    public boolean isWin() {
        return getWin();
    }
}
