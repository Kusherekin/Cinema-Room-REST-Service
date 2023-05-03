package cinema.models;

import java.util.UUID;

public class Seat {
    private int row;
    private int column;
    private boolean booked;
    private int price;
    private UUID token;

    public Seat(int row, int column, boolean booked, int price) {
        this.row = row;
        this.column = column;
        this.booked = booked;
        this.price = price;
        this.token = UUID.randomUUID();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken() {
        this.token = UUID.randomUUID();
    }

}