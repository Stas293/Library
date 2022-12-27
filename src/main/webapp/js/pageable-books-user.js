

const makeRow = (rowData, index, callback) => {
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');
    let label = document.createElement('label');
    let hiddenId = `hidden-request-data-${index}`;

    if (rowData.name) {
        tableRow.appendChild(createHiddenOrderRegisterDiv(hiddenId, rowData, callback));
    } else if (rowData.book.name) {
        tableRow.appendChild(createHiddenRequestDiv(hiddenId, rowData));
    }

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

    label.appendChild(document.createTextNode(rowData.name ? rowData.name : rowData.book.name));
    anchor.appendChild(label);
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.style.width = '25%';
    tableData.appendChild(
        document
            .createTextNode(rowData.isbn ? rowData.isbn : rowData.book.isbn));
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.style.width = '25%';
    tableData.appendChild(
        document
            .createTextNode(rowData.publicationDate ? rowData.publicationDate : rowData.book.publicationDate));
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.style.width = '25%';

    let authorsString = rowData.authors ? rowData.authors.map(author => `${author.firstName} ${author.lastName}`).join(', ') : rowData.book.authors.map(author => `${author.firstName} ${author.lastName}`).join(', ');
    tableData.appendChild(
        document
            .createTextNode(
                authorsString))
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

const createHiddenOrderRegisterDiv = (hiddenId, rowData) => {

    let {hiddenRequestDiv, hiddenDesk, fieldHeading} = initPopup(hiddenId);
    fieldHeading.appendChild(document.createTextNode(rowData.name));
    hiddenDesk.appendChild(fieldHeading);

    fieldHeading = document.createElement('h2');
    let field = document.createElement('span');
    field.appendChild(document.createTextNode("ISBN: " + rowData.isbn));
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    field = document.createElement('h2');
    const authorsString = rowData.authors.map(author => `${author.firstName} ${author.lastName}`).join(', ');
    field.appendChild(document.createTextNode("Authors: " + authorsString));
    hiddenDesk.appendChild(field);

    field = document.createElement('h5');
    field.setAttribute('title','publication date');
    field.appendChild(document.createTextNode("Publication date: " + `${rowData.publicationDate}`));
    hiddenDesk.appendChild(field);

    field = document.createElement('h5');
    field.setAttribute('title','fine per day');
    field.appendChild(document.createTextNode("Fine: " + `${rowData.fine}`));
    hiddenDesk.appendChild(field);

    addFormOrder(hiddenDesk, rowData, hiddenId);


    hiddenRequestDiv.appendChild(hiddenDesk);

    return hiddenRequestDiv;
}

const createHiddenRequestDiv = (hiddenId, rowData) => {
    let {hiddenRequestDiv, hiddenDesk, fieldHeading} = initPopup(hiddenId);
    fieldHeading.appendChild(document.createTextNode(rowData.book.name));
    hiddenDesk.appendChild(fieldHeading);

    fieldHeading = document.createElement('h2');
    let field = document.createElement('span');
    field.appendChild(document.createTextNode("ISBN: " + rowData.book.isbn));
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    field = document.createElement('h2');
    field.appendChild(document.createTextNode("Date of order: " + rowData.dateCreated));
    fieldHeading.appendChild(field);

    field = document.createElement('h2');
    field.appendChild(document.createTextNode("Name of place: " + rowData.place.data));
    fieldHeading.appendChild(field);

    field = document.createElement('h2');
    field.appendChild(document.createTextNode("Status: " + rowData.status.value));
    fieldHeading.appendChild(field);

    if (rowData.dateExpire !== "Not set") {
        field = document.createElement('h2');
        field.appendChild(document.createTextNode("Date expire: " + rowData.dateExpire));
        fieldHeading.appendChild(field);

        if (rowData.priceOverdue > 0) {
            field = document.createElement('h2');
            field.appendChild(document.createTextNode("Fine: " + rowData.priceOverdue));
            fieldHeading.appendChild(field);
        }
    }

    hiddenRequestDiv.appendChild(hiddenDesk);

    return hiddenRequestDiv;
}