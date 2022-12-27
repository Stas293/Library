const urlPath = `/library/librarian-order?statusCode=REGISTER`;
const urlEditRequest = `/library/librarian/edit-order`;
let urlAcceptedOrders = `/library/librarian-order?statusCode=ACCEPT`;

window.onload = () => {
    document.getElementById('date_expire').setAttribute('style','display:none');
    setOrderListeners(urlPath);
    wizard(urlPath, updateOrder);
}

function showUsersOrders() {
    document.getElementById('date_expire').setAttribute('style','display:none');
    document.getElementById('date_created').setAttribute('style','display');
    setOrderListeners(urlPath);
    wizard(urlPath, updateOrder);
}

function showAcceptedOrdersOnSubscription() {
    document.getElementById('date_expire').setAttribute('style','display');
    document.getElementById('date_created').setAttribute('style','display:none');
    setOrderListeners(urlAcceptedOrders + '&placeName=On a subscription');
    wizard(urlAcceptedOrders + '&placeName=On a subscription');
}

function showAcceptedOrdersReadingRoom() {
    document.getElementById('date_expire').setAttribute('style','display');
    document.getElementById('date_created').setAttribute('style','display:none');
    setOrderListeners(urlAcceptedOrders + '&placeName=To the reading room');
    wizard(urlAcceptedOrders + '&placeName=To the reading room');
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
    let button = document.createElement('input');
    rowData.status.nextStatuses.map((nextStatus) => {
        let option = document.createElement('option');
        option.setAttribute('value', nextStatus.key);
        option.appendChild(document.createTextNode(nextStatus.value));
        select.appendChild(option);
    });
    if (rowData.status.code !== 'ACCEPT') {
        let input = document.createElement('input');
        input.setAttribute('id', `input-${hiddenId}`);
        input.setAttribute('name', 'dateExpire');
        input.setAttribute('type', 'date');
        if (rowData.place.name === 'To the reading room') {
            input.style.display = 'none';
        } else {
            input.setAttribute('min', new Date().toISOString().split('T')[0]);
        }
        form.appendChild(input)
    }
    select.setAttribute('name', 'status');
    select.setAttribute('id',`select-${hiddenId}`);
    select.setAttribute('class','form-control');
    select.setAttribute('style','background: white; margin: 10px 0; border: 1px solid #dee2e6;');
    button.setAttribute('type', 'submit');
    button.setAttribute('class','btn btn-form-submit');
    form.setAttribute('action',urlEditRequest);
    form.appendChild(select);
    form.appendChild(button);
    hiddenDesk.appendChild(form);
}