package fun.vyse.cloud.design.constant;


public enum EntityType {
    MODEL("entity",1),
    COMPONENT("component",2);

    private EntityType(String code,int type){
        this.code = code;
        this.type = type;
    }

    private final String code;
    private final int type;

    public String getCode(){
        return this.code;
    }

    public int getType(){
        return this.type;
    }
}
