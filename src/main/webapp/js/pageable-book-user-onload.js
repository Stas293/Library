let urlPath = "";
let urlMakeOrders = `/library/order/user/books-to-order`;
const urlRegisteredOrders = `/library/order/user/get-order?statusCode=REGISTER`;
const urlAcceptedOrders = `/library/order/user/get-order?statusCode=ACCEPT`;
const createOrderPath = `/library/order/user`;
const urlGetOrder = `/library/order/user/view`;

window.onload = () => {
    urlPath = urlMakeOrders;
    setBookListeners(urlPath);
    wizard(urlPath);
}

function setTableHeadersDefault() {
    let isbn = document.querySelector(`#isbn`);
    let date_publication = document.querySelector(`#date_publication`);
    let language = document.querySelector(`#language`);

    isbn.innerHTML = document.querySelector(`#label_ISBN`).innerHTML;
    date_publication.innerHTML = document.querySelector(`#label_date_publication`).innerHTML;
    language.innerHTML = document.querySelector(`#label_language`).innerHTML;
}

const listOrderToChoose = () => {
    setSize();
    clearTextFields();
    urlPath = urlMakeOrders;
    setBookListeners(urlPath);
    setTableHeadersDefault();
    wizard(urlPath);
}

function setTableHeadersGetOrder() {
    let isbn = document.querySelector(`#isbn`);
    let date_publication = document.querySelector(`#date_publication`);
    let language = document.querySelector(`#language`);

    isbn.innerHTML = document.querySelector(`#label_date_created`).innerHTML;
    date_publication.innerHTML = document.querySelector(`#label_place`).innerHTML;
    language.innerHTML = document.querySelector(`#label_fine`).innerHTML;
}

const listRegisteredOrders = () => {
    setSize();
    clearTextFields();
    urlPath = urlRegisteredOrders;
    setBookListeners(urlRegisteredOrders);
    setTableHeadersGetOrder();
    wizard(urlRegisteredOrders);
}

const listAcceptedOrder = () => {
    setSize();
    clearTextFields();
    urlPath = urlAcceptedOrders;
    setBookListeners(urlAcceptedOrders);
    wizard(urlAcceptedOrders);
}

class Book {
    constructor(id, title, isbn, publicationDate, language, fine) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = new Date(publicationDate);
        this.language = language;
        this.fine = fine;
    }

    static from = function (rowData) {
        return new Book(
            rowData.id,
            rowData.title,
            rowData.isbn,
            rowData.publicationDate,
            rowData.language,
            rowData.fine);
    };
}

class Order {
    constructor(id, dateCreated, dateExpire, book, status, user, place) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.dateExpire = dateExpire;
        this.book = book;
        this.place = place;
    }

    static from = function (rowData) {
        return new Order(
            rowData.id,
            rowData.dateCreated,
            rowData.dateExpire,
            Book.from(rowData.book),
            Place.from(rowData.place)
        )
    }
}

class Place {
    constructor(id, name, defaultDays, choosable) {
        this.id = id;
        this.name = name;
        this.defaultDays = defaultDays;
        this.choosable = choosable;
    }

    static from = function (rowData) {
        return new Place(
            rowData.id,
            rowData.name,
            rowData.defaultDays,
            rowData.choosable);
    }
}


function makeRowOrder(rowData) {
    console.log(rowData);
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');

    anchor.setAttribute('href', `${urlGetOrder}/${rowData.id}`);
    anchor.appendChild(document.createTextNode(rowData.book.title));
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(document.createTextNode(rowData.dateCreated));
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.appendChild(document.createTextNode(rowData.place.name));
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.appendChild(document.createTextNode(rowData.book.fine));
    tableRow.appendChild(tableData);
    return tableRow;
}

const makeRow = (rowData, index) => {
    if (urlPath === urlRegisteredOrders) {
        return makeRowOrder(rowData);
    }
    rowData = Book.from(rowData);
    console.log(rowData);
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');

    anchor.setAttribute('href', `${createOrderPath}/${rowData.id}`);
    anchor.appendChild(document.createTextNode(rowData.title));
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(document
        .createTextNode(rowData.isbn));
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.appendChild(document
        .createTextNode(rowData.publicationDate.toLocaleDateString()));
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.appendChild(document
        .createTextNode(rowData.language));
    tableRow.appendChild(tableData);
    return tableRow;
}