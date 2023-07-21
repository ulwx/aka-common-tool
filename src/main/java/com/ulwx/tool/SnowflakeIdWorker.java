package com.ulwx.tool;


import com.ulwx.tool.id.IdWorker;

public class SnowflakeIdWorker {

	public static SnowflakeIdWorker instance= new SnowflakeIdWorker();

    private IdWorker idWorker=null;
    public SnowflakeIdWorker() {
        idWorker=new IdWorker(null);
    }

    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public  long nextId() {
        return idWorker.nextId();
    }



    //==============================Test=============================================
    /** 测试 */
    public static void main(String[] args) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker();
        for (int i = 0; i < 1000; i++) {
            long id = idWorker.nextId();
            System.out.println(id);
        }
    }
}