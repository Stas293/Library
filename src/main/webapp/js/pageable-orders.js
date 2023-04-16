
const makeRow = (rowData, index) => {
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');
    let label = document.createElement('label');
    let hiddenId = `hidden-request-data-${index}`;

    tableRow.appendChild(createHiddenRequestDiv(hiddenId, rowData));

    anchor.setAttribute('href', '#');
    anchor.onclick = () => {
        let div = document.getElementById(hiddenId);
        div.style.width = '100vw';
        div.style.height = '100vh';
        div.style.display = 'flex';
        div.style.position = 'absolute';
        div.style.top = "0";
        div.style.left = "0";
        div.style.background = 'rgba(3,3,3,0.7)';
        div.style.zIndex = "2000";
    }

    label.setAttribute('id', 'view-request-modal-window');

    label.appendChild(document.createTextNode(rowData.user.login));
    anchor.appendChild(label);
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.style.width = '25%';
    tableData.appendChild(
        document
            .createTextNode(rowData.book.name));
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.style.width = '25%';
    tableData.appendChild(
        document
            .createTextNode(Date.parse(rowData.dateExpire) ? rowData.dateExpire : rowData.dateCreated));
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.style.width = '25%';

    tableData.appendChild(
        document
            .createTextNode(
                rowData.book.isbn));
    tableRow.appendChild(tableData);
    return tableRow;
}

function initPopup(hiddenId) {
    let hiddenRequestDiv = document.createElement('div');

    hiddenRequestDiv.id = hiddenId;
    hiddenRequestDiv.style.display = 'none';

    let hiddenDesk = document.createElement('div');
    hiddenDesk.className = 'order-form';
    hiddenDesk.style.textAlign = "left";
    hiddenDesk.style.padding = "30px 15px";

    let labelClose = document.createElement('button');
    labelClose.type = 'button';
    labelClose.className = 'btn-close';
    labelClose.ariaLabel = 'Close';
    labelClose.onclick = () => {
        let div = document.getElementById(hiddenId);
        div.style.display = 'none';
    }
    hiddenDesk.appendChild(labelClose);

    let fieldHeading = document.createElement('h1');
    return {hiddenRequestDiv, hiddenDesk, fieldHeading};
}

const createHiddenRequestDiv = (hiddenId, rowData) => {
    let {hiddenRequestDiv, hiddenDesk, fieldHeading} = initPopup(hiddenId);
    fieldHeading.appendChild(document.createTextNode(rowData.book.name));
    hiddenDesk.appendChild(fieldHeading);

    fieldHeading = document.createElement('h2');
    let field = document.createElement('span');
    var value = document.getElementById('label_ISBN').innerHTML;
    field.appendChild(document.createTextNode(value + ": " + rowData.book.isbn));
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    field = document.createElement('h2');
    value = document.getElementById('date_created').innerHTML;
    field.appendChild(document.createTextNode(value + ": " + rowData.dateCreated));
    fieldHeading.appendChild(field);

    field = document.createElement('h2');
    value = document.getElementById('place').innerHTML;
    field.appendChild(document.createTextNode(value + ": " + rowData.place.data));
    fieldHeading.appendChild(field);

    field = document.createElement('h2');
    value = document.getElementById('status').innerHTML;
    field.appendChild(document.createTextNode(value + ": " + rowData.status.value));
    fieldHeading.appendChild(field);

    field = document.createElement('h2');
    value = document.getElementById('date_expires').innerHTML;
    field.appendChild(document.createTextNode(value + ": " + rowData.dateExpire));
    fieldHeading.appendChild(field);

    if (rowData.priceOverdue !== "0") {
        field = document.createElement('h2');
        value = document.getElementById('fine').innerHTML;
        field.appendChild(document.createTextNode(value + ": " + rowData.priceOverdue));
        fieldHeading.appendChild(field);
    }

    updateOrder(hiddenDesk, rowData, hiddenId);

    hiddenRequestDiv.appendChild(hiddenDesk);

    return hiddenRequestDiv;
}