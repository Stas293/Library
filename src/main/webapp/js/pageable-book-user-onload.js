let urlPath = "";
let urlMakeOrders = `/library/user/books-to-order`;
const createOrderPath = `/library/user/new-order`;
let places;
const urlGetPlaces = `/library/get-places`;
const urlRegisteredOrders = `/library/user/order?statusCode=REGISTER`;
const urlAcceptedOrders = `/library/user/order?statusCode=ACCEPT`;

window.onload = () => {
    ajaxJS(urlGetPlaces, (data) => {
        places = data;
    });
    urlPath = urlMakeOrders;
    setBookListeners(urlPath);
    wizard(urlPath, null);
}

const listOrderToChoose = () => {
    setSize();
    clearTextFields();
    urlPath = urlMakeOrders;
    setBookListeners(urlPath);
    wizard(urlPath, null);
}

const listRegisteredOrders = () => {
    setSize();
    clearTextFields();
    urlPath = urlRegisteredOrders;
    setBookListeners(urlRegisteredOrders);
    wizard(urlRegisteredOrders, null);
}

const listAcceptedOrder = () => {
    setSize();
    clearTextFields();
    urlPath = urlAcceptedOrders;
    setBookListeners(urlAcceptedOrders);
    wizard(urlAcceptedOrders, null);
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
    button.innerHTML = document.getElementById('make_order').innerHTML;

    let radioInput = document.createElement('input');
    radioInput.type = 'radio';
    radioInput.name = 'placeName';

    for (const element of places) {
        const div = document.createElement('div');
        const label = document.createElement('label');
        const clone = radioInput.cloneNode(true)
        clone.setAttribute('value', element.name);
        clone.setAttribute('id', `place-${element.id}`);
        label.setAttribute('for', `place-${element.id}`);
        label.appendChild(document.createTextNode(element.data));
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

