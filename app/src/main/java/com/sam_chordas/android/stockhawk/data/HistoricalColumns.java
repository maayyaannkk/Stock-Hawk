package com.sam_chordas.android.stockhawk.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by maayy on 03-06-2016.
 */
public class HistoricalColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull @References(table = QuoteDatabase.QUOTES,column = QuoteColumns.SYMBOL)
    public static final String SYMBOL = "symbol";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String DATE = "date";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String OPEN = "open";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String CLOSE = "close";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String HIGH = "high";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String LOW = "low";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String VOLUME = "volume";
}
