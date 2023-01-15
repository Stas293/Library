const urlPath = `/library/admin/authors-page`;

window.onload = () => {
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

    label.appendChild(document.createTextNode(rowData.id));
    anchor.appendChild(label);
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.firstName));
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.lastName));
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

    fieldHeading = document.createElement('h3');
    let field = document.createElement('span');
    field.appendChild(document.createTextNode(rowData.firstName));
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    fieldHeading = document.createElement('h3');
    field = document.createElement('span');
    field.appendChild(document.createTextNode(rowData.lastName));
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    field = document.createElement('button');
    field.className = 'btn btn-primary';
    field.appendChild(document.createTextNode(document.getElementById('edit_author_button').innerText));
    field.onclick = () => {
        let div = document.getElementById(hiddenId);
        div.style.display = 'none';
        window.location.href = `/library/admin/author/edit/${rowData.id}`;
    }
    fieldHeading.appendChild(document.createElement('br'));
    hiddenDesk.appendChild(fieldHeading);
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    field = document.createElement('button');
    field.className = 'btn btn-danger';
    field.appendChild(document.createTextNode(document.getElementById("delete_author_button").innerText));
    field.onclick = () => {
        let div = document.getElementById(hiddenId);
        div.style.display = 'none';
        window.location.href = `/library/admin/author/delete/${rowData.id}`;
    }
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    hiddenRequestDiv.appendChild(hiddenDesk);

    return hiddenRequestDiv;
}