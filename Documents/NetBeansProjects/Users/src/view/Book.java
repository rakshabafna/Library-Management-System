/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author raksha
 */
public class Book {
    
int id,quantity;
        String title,genre,author,isbn, description,imagebase64;
        float price;
        public Book(int id,int quantity,String title,String genre,String author,String isbn,String imagebase64,String description,float price){
            this.id=id;
            this.quantity=quantity;
            this.author=author;
            this.title=title;
            this.genre=genre;
            this.isbn=isbn;
            this.imagebase64=imagebase64;
            this.description=description;
            this.price=price;
        }
    
}
