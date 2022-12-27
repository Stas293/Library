const urlPath = `/library/books-to-order`;
const createOrderPath = `/library/user/new-order`;
let places;
const urlGetPlaces = `/library/get-places`;
const urlRegisteredOrders = `/library/user/order?statusCode=REGISTER`;
const urlAcceptedOrders = `/library/user/order?statusCode=ACCEPT`;

window.onload = () => {
    ajaxJS(urlGetPlaces, (data) => {
        places = data.content;
    });
    setBookListeners(urlPath);
    wizard(urlPath, addFormOrder);
}

const listOrderToChoose = () => {
    setSize();
    clearTextFields();
    setBookListeners(urlPath);
    wizard(urlPath, addFormOrder);
}

const listRegisteredOrders = () => {
    setSize();
    clearTextFields();
    setBookListeners(urlRegisteredOrders);
    wizard(urlRegisteredOrders);
}

const listAcceptedOrder = () => {
    setSize();
    clearTextFields();
    setBookListeners(urlAcceptedOrders);
    wizard(urlAcceptedOrders);
}

const addFormOrder = (hiddenDesk, rowData, hiddenId) => {
    let form = document.createElement('form');
    form.id = `form-${hiddenId}`;
    let bookId = document.createElement('input');
    bookId.name = 'id';
    bookId.value = rowData.id;
    bookId.style.display = 'none';
    form.appendChild(bookId);
    let button = document.createElement('button');
    button.type = 'submit';
    button.className = 'btn btn-primary';
    button.innerHTML = 'Order';

    var radioInput = document.createElement('input');
    radioInput.type = 'radio';
    radioInput.name = 'placeName';

    for (var i = 0; i < places.length; i++) {
        var div = document.createElement('div');
        var label = document.createElement('label');
        var clone = radioInput.cloneNode(true);
        clone.setAttribute('value', places[i].name);
        clone.setAttribute('id', `place-${places[i].id}`);
        label.setAttribute('for', `place-${places[i].id}`);
        label.appendChild(document.createTextNode(places[i].data));
        div.appendChild(clone);
        div.appendChild(label);
        form.appendChild(div);
    }

    form.setAttribute('action', createOrderPath);
    form.setAttribute('method', 'POST');
    form.appendChild(button);
    console.log(rowData);
    hiddenDesk.appendChild(form);
}

