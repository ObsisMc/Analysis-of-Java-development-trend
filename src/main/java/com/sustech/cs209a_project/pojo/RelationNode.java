package com.sustech.cs209a_project.pojo;

public class RelationNode {
    public int id;
    public String name;
    public double symbolSize;
    public double value;
    public float x;
    public float y;
    public int category;

    public RelationNode(int id, String name, double symbolSize, double value, float x, float y, int category) {
        this.id = id;
        this.name = name;
        this.symbolSize = symbolSize;
        this.value = value;
        this.x = x;
        this.y = y;
        this.category = category;
    }

    static class RelationEdge{
        public int sourceId;
        public int targetId;
        public RelationEdge(int sourceId, int targetId){
            this.sourceId = sourceId;
            this.targetId = targetId;
        }
    }

}


