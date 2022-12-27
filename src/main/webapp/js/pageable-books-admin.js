const urlPath = `/library/books-page`;

window.onload = () => {
    setBookListeners(urlPath);
    wizard(urlPath);
}

const makeRow = (rowData, index) => {
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');
    let label = document.createElement('label');
    let hiddenId = `hidden-order-data-${index}`;

    tableRow.appendChild(createHiddenBookDiv(hiddenId, rowData));

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

    label.setAttribute('id', 'view-order-modal-window');

    label.appendChild(document.createTextNode(rowData.name));
    anchor.appendChild(label);
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.isbn))
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.publicationDate))
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.style.width = '25%';
    let authorsString = rowData.authors.map(author => `${author.firstName} ${author.lastName}`).join(', ');
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

const createHiddenBookDiv = (hiddenId, rowData) => {
    let {hiddenRequestDiv, hiddenDesk, fieldHeading} = initPopup(hiddenId);

    fieldHeading.appendChild(document.createTextNode("Id: " + rowData.id));
    hiddenDesk.appendChild(fieldHeading);

    fieldHeading = document.createElement('h2');
    let field = document.createElement('span');
    field.appendChild(document.createTextNode(rowData.name));
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    fieldHeading = document.createElement('h2');
    field = document.createElement('span');
    field.appendChild(document.createTextNode(rowData.count));
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    fieldHeading = document.createElement('h2');
    field = document.createElement('span');
    field.appendChild(document.createTextNode(rowData.isbn));
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    field = document.createElement('h2');
    field.appendChild(document.createTextNode(rowData.publicationDate));
    fieldHeading.appendChild(field);

    field = document.createElement('h2');
    field.appendChild(document.createTextNode(rowData.fine));
    fieldHeading.appendChild(field);

    field = document.createElement('h2');
    field.appendChild(document.createTextNode(rowData.language));
    fieldHeading.appendChild(field);

    field = document.createElement('h2');
    field.appendChild(document.createTextNode(rowData.authors.map(author => `${author.firstName} ${author.lastName}`).join(', ')));
    fieldHeading.appendChild(field);

    field = document.createElement('button');
    field.className = 'btn btn-primary';
    field.appendChild(document.createTextNode('Edit'));
    field.onclick = () => {
        let div = document.getElementById(hiddenId);
        div.style.display = 'none';
        window.location.href = `/library/admin/book/edit/${rowData.id}`;
    }
    fieldHeading.appendChild(field);

    field = document.createElement('button');
    field.className = 'btn btn-danger';
    field.appendChild(document.createTextNode('Delete'));
    field.onclick = () => {
        let div = document.getElementById(hiddenId);
        div.style.display = 'none';
        window.location.href = `/library/admin/book/delete/${rowData.id}`;
    }
    fieldHeading.appendChild(field);

    hiddenRequestDiv.appendChild(hiddenDesk);

    return hiddenRequestDiv;
}