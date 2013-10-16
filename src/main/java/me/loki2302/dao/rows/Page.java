package me.loki2302.dao.rows;

import java.util.List;

public class Page<T> {
    public int NumberOfItems;
    public int ItemsPerPage;
    public int NumberOfPages;
    public int CurrentPage;
    public List<T> Items;
    
    public boolean hasPreviousPage() {
        return CurrentPage > 0;
    }
    
    public boolean hasNextPage() {
        return CurrentPage < NumberOfPages - 1;
    }
    
    @Override
    public String toString() {
        return String.format(
                "Page{NumberOfItems=%d, ItemsPerPage=%d, NumberOfPages=%d, CurrentPage=%d}",
                NumberOfItems,
                ItemsPerPage,
                NumberOfPages,
                CurrentPage);                
    }
}