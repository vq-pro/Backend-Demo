package quebec.virtualite.backend.services.domain.database;

public interface DatabaseTables
{
    String SUFFIX_SEQUENCE = "_id_seq";

    String WHEELS_TABLE = "wheels";
    String WHEELS_SEQUENCE = WHEELS_TABLE + SUFFIX_SEQUENCE;
}
