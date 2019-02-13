package com.albert.cursorhelper;

import android.database.Cursor;

public interface CursorHandler<T> {

    T handle(Cursor cursor);
}
