let urlPath = ''
const registeresOrders = `/library/librarian-order?statusCode=REGISTER`;
const urlEditRequest = `/library/librarian/edit-order`;
let urlAcceptedOrders = `/library/librarian-order?statusCode=ACCEPT`;


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

const updateOrder = (hiddenDesk, rowData, hiddenId) => {
    let form = document.createElement('form');
    form.setAttribute('id',`form-${hiddenId}`);
    let requestId = document.createElement('input');
    requestId.setAttribute('name','id');
    requestId.setAttribute('value', rowData.id);
    requestId.setAttribute('style','display:none');
    form.appendChild(requestId);
    let select = document.createElement('select');
    let button = document.createElement('button');
    rowData.status.nextStatuses.map((nextStatus) => {
        let option = document.createElement('option');
        option.setAttribute('value', nextStatus.key);
        option.appendChild(document.createTextNode(nextStatus.value));
        select.appendChild(option);
    });
    if (rowData.chooseDateExpire === true) {
        let input = document.createElement('input');
        input.setAttribute('id', `input-${hiddenId}`);
        input.setAttribute('name', 'dateExpire');
        input.setAttribute('type', 'date');
        input.setAttribute('min', new Date().toISOString().split('T')[0]);
        input.required = true;
        form.appendChild(input)
    }
    select.setAttribute('name', 'status');
    select.setAttribute('id',`select-${hiddenId}`);
    select.setAttribute('class','form-control');
    select.setAttribute('style','background: white; margin: 10px 0; border: 1px solid #dee2e6;');
    button.setAttribute('type', 'submit');
    button.setAttribute('class', 'btn btn-primary');
    button.setAttribute('style', 'margin: 10px 0;');
    button.appendChild(document.createTextNode(document.getElementById('edit_order').innerHTML));
    form.setAttribute('action',urlEditRequest);
    form.setAttribute('method','POST');
    form.appendChild(select);
    form.appendChild(button);
    hiddenDesk.appendChild(form);
}