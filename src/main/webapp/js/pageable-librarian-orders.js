let urlPath = ''
const registeresOrders = `/library/order/librarian?statusCode=REGISTER`;
const urlEditRequest = `/library/order/librarian/edit-order`;
let urlAcceptedOrders = `/library/order/librarian?statusCode=ACCEPT`;


window.onload = () => {
    document.getElementById('date_expire').setAttribute('style','display:none');
    urlPath = registeresOrders;
    setOrderListeners(urlPath);
    wizard(urlPath, null);
}

function showUsersOrders() {
    document.getElementById('date_expire').setAttribute('style','display:none');
    document.getElementById('date_created').setAttribute('style','display');
    urlPath = registeresOrders;
    setOrderListeners(urlPath);
    wizard(urlPath, null);
}

function showAcceptedOrdersOnSubscription() {
    document.getElementById('date_expire').setAttribute('style','display');
    document.getElementById('date_created').setAttribute('style','display:none');
    urlPath = urlAcceptedOrders + '&placeName=On a subscription';
    setOrderListeners(urlPath);
    wizard(urlPath, null);
}

function showAcceptedOrdersReadingRoom() {
    document.getElementById('date_expire').setAttribute('style','display');
    document.getElementById('date_created').setAttribute('style','display:none');
    urlPath = urlAcceptedOrders + '&placeName=To the reading room';
    setOrderListeners(urlPath);
    wizard(urlPath, null);
}

function setOrderListeners(url) {
    let login = document.querySelector(`#login`);
    let book_name = document.querySelector(`#book_name`);
    let date_created = document.querySelector(`#date_created`);
    let isbn = document.querySelector(`#isbn`);
    let date_expire = document.querySelector(`#date_expire`);
    login.onclick = () => {
        sortBy = 'login';
        func(url);
    }
    book_name.onclick = () => {
        sortBy = 'book_name';
        func(url);
    }
    isbn.onclick = () => {
        sortBy = 'ISBN';
        func(url);
    }
    date_created.onclick = () => {
        sortBy = "date_created";
        func(url);
    }
    date_expire.onclick = () => {
        sortBy = "date_expire";
        func(url);
    }
}

class Order {
    constructor(dateCreated, dateExpire, book, status, user, place) {
        this.dateCreated = new Date(dateCreated);
        this.dateExpire = new Date(dateExpire);
        this.book = book;
        this.status = status;
        this.user = user;
        this.place = place;
    }

    static from(json) {
        return new Order(
            json.dateCreated,
            json.dateExpire,
            json.book,
            json.status,
            json.user,
            json.place
        );
    }
}

const makeRow = (rowData) => {
    let order = Order.from(rowData);
    console.log(rowData);
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');

    anchor.setAttribute('href', `${urlEditRequest}/${rowData.id}`);
    anchor.appendChild(document.createTextNode(order.user.login));
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(order.book.title));
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(order.dateCreated.toDateString()));
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');

    tableData.appendChild(
        document
            .createTextNode(
                order.book.isbn));
    tableRow.appendChild(tableData);
    return tableRow;
}