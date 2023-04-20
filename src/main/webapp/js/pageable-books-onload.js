let urlPath = "/library/books/page";
let books = "books";

window.onload = function () {
    setBookListeners(urlPath);
    wizard(urlPath);
};

class Book {
    constructor(id, title, isbn, publicationDate, language, authors) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = new Date(publicationDate);
        this.language = language;
        this.authors = authors;
    }

    static from = function (rowData) {
        return new Book(
            rowData.id,
            rowData.title,
            rowData.isbn,
            rowData.publicationDate,
            rowData.language,
            rowData.authors);
    };
}

const makeRow = function (rowData) {
    let book = Book.from(rowData);
    console.log(book);
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');
    anchor.setAttribute('href', `${books}/${book.id}`);
    anchor.appendChild(document.createTextNode(book.title));
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.appendChild(document
        .createTextNode(book.isbn));
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.appendChild(document
        .createTextNode(book.publicationDate.toLocaleDateString()));
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.appendChild(document
        .createTextNode(book.authors));
    tableRow.appendChild(tableData);
    return tableRow;
};
