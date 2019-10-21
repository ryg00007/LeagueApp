package stir.ac.uk.leaguestatsapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChampionList {

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "Name")
    public String Name;
}
