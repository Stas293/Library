let urlPath = "";
let urlMakeOrders = `/library/order/user/books-to-order`;
const urlRegisteredOrders = `/library/order/user/get-order?statusCode=REGISTER`;
const urlAcceptedOrders = `/library/order/user/get-order?statusCode=ACCEPT`;
const createOrderPath = `/library/order/user`;
const urlGetOrder = `/library/order/user/view`;

function setUserPageBookListeners() {
    const book_title = document.querySelector("#book_title");
    const isbn = document.querySelector("#isbn");
    const date_publication = document.querySelector("#date_publication");
    const book_authors = document.querySelector("#book_authors");
    const date_created = document.querySelector("#date_created");
    const place = document.querySelector("#place");
    const date_expire = document.querySelector("#date_expire");
    const fine = document.querySelector("#fine");
    date_created.onclick = function () {
        sortBy = "date_created";
        func(urlPath);
    }
    date_expire.onclick = function () {
        sortBy = "date_expire";
        func(urlPath);
    }
    place.onclick = function () {
        sortBy = "code";
        func(urlPath);
    }
    book_title.onclick = function () {
        sortBy = "title";
        func(urlPath);
    };
    isbn.onclick = function () {
        sortBy = "isbn";
        func(urlPath);
    };
    date_publication.onclick = function () {
        sortBy = "date_publication";
        func(urlPath);
    };
    book_authors.onclick = function () {
        sortBy = "last_name";
        func(urlPath);
    };
    fine.onclick = function () {
        sortBy = "fine";
        func(urlPath);
    }
    sortBy = "title";
}

window.onload = () => {
    urlPath = urlMakeOrders;
    setTableHeadersDefault();
    setUserPageBookListeners();
    wizard(urlPath);
}

function setTableHeadersDefault() {
    document.querySelector("#book_title").setAttribute('style', 'display');
    document.querySelector("#isbn").setAttribute('style', 'display');
    document.querySelector("#date_publication").setAttribute('style', 'display');
    document.querySelector("#book_authors").setAttribute('style', 'display');
    document.querySelector("#date_created").style.display = "none";
    document.querySelector("#place").style.display = "none";
    document.querySelector("#date_expire").style.display = "none";
    document.querySelector("#fine").style.display = "none";
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
    document.querySelector("#book_title").setAttribute('style', 'display');
    document.querySelector("#isbn").setAttribute('style', 'display:none');
    document.querySelector("#date_publication").setAttribute('style', 'display:none');
    document.querySelector("#book_authors").setAttribute('style', 'display:none');
    document.querySelector("#date_created").setAttribute('style', 'display');
    document.querySelector("#place").setAttribute('style', 'display');
    document.querySelector("#date_expire").setAttribute('style', 'display:none');
    document.querySelector("#fine").setAttribute('style', 'display');
}

const listRegisteredOrders = () => {
    setSize();
    clearTextFields();
    urlPath = urlRegisteredOrders;
    setBookListeners(urlRegisteredOrders);
    setTableHeadersGetOrder();
    wizard(urlRegisteredOrders);
}

function setTableHeadersAcceptOrder() {
    document.querySelector("#book_title").setAttribute('style', 'display');
    document.querySelector("#isbn").setAttribute('style', 'display:none');
    document.querySelector("#date_publication").setAttribute('style', 'display:none');
    document.querySelector("#book_authors").setAttribute('style', 'display:none');
    document.querySelector("#date_created").setAttribute('style', 'display:none');
    document.querySelector("#place").setAttribute('style', 'display');
    document.querySelector("#date_expire").setAttribute('style', 'display');
    document.querySelector("#fine").setAttribute('style', 'display');
}

const listAcceptedOrder = () => {
    setSize();
    clearTextFields();
    urlPath = urlAcceptedOrders;
    setBookListeners(urlAcceptedOrders);
    setTableHeadersAcceptOrder();
    wizard(urlAcceptedOrders);
}

class Book {
    constructor(id, title, isbn, publicationDate, language, fine, authors) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = new Date(publicationDate);
        this.language = language;
        this.fine = fine;
        this.authors = authors;
    }

    static from = function (rowData) {
        return new Book(
            rowData.id,
            rowData.title,
            rowData.isbn,
            rowData.publicationDate,
            rowData.language,
            rowData.fine,
            rowData.authors);
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

function makeRowOrderAccepted(rowData) {
    console.log(rowData);
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');

    anchor.setAttribute('href', `${urlGetOrder}/${rowData.id}`);
    anchor.appendChild(document.createTextNode(rowData.book.title));
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(document.createTextNode(rowData.dateExpire));
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
    console.log(rowData);
    if (urlPath === urlRegisteredOrders) {
        return makeRowOrder(rowData);
    } else if (urlPath === urlAcceptedOrders) {
        return makeRowOrderAccepted(rowData);
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
        .createTextNode(rowData.authors));
    tableRow.appendChild(tableData);
    return tableRow;
}