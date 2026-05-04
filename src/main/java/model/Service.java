package model;

public class Service 
{
    private int serviceId;
    private String title, description, label, durationText, features, icon;
    private double price;

    // Constructor
    public Service(int serviceId, String title, String description, String label, double price, String durationText, String features, String icon) 
    {
        this.serviceId = serviceId;
        this.title = title;
        this.description = description;
        this.label = label;
        this.price = price;
        this.durationText = durationText;
        this.features = features;
        this.icon = icon;
    }

    // Getters and Setters for each field
    public int getServiceId() { return serviceId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLabel() { return label; }
    public double getPrice() { return price; }
    
    public void setPrice(double price) { this.price = price; }
    
    public String getDurationText() { return durationText; }
    public String getFeatures() { return features; }
    public String getIcon() { return icon; }
}