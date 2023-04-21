const urlPath = `/library/authors/admin`;

window.onload = () => {
    wizard(urlPath);
}

const createModal = (rowData, index, urlPath) => {
    const modal = document.createElement('div');
    modal.className = 'modal fade';
    modal.id = `author${index}`;

    const modalDialog = document.createElement('div');
    modalDialog.className = 'modal-dialog modal-dialog-centered';

    const modalContent = document.createElement('div');
    modalContent.className = 'modal-content';

    const modalHeader = document.createElement('div');
    modalHeader.className = 'modal-header';

    const modalTitle = document.createElement('h5');
    modalTitle.className = 'modal-title';
    modalTitle.innerHTML = rowData.id;

    const closeBtn = document.createElement('button');
    closeBtn.className = 'btn-close';
    closeBtn.setAttribute('data-bs-dismiss', 'modal');

    const modalBody = document.createElement('div');
    modalBody.className = 'modal-body';

    const firstName = document.createElement('p');
    firstName.innerHTML = rowData.firstName;
    modalBody.appendChild(firstName);

    const middleName = document.createElement('p');
    middleName.innerHTML = rowData.middleName;
    modalBody.appendChild(middleName);

    const lastName = document.createElement('p');
    lastName.innerHTML = rowData.lastName;
    modalBody.appendChild(lastName);

    const modalFooter = document.createElement('div');
    modalFooter.className = 'modal-footer';

    const editBtn = document.createElement('a');
    editBtn.className = 'btn btn-warning';
    editBtn.innerHTML = document.getElementById("edit_author_button").innerHTML;
    editBtn.href = urlPath + `/${rowData.id}/edit`;
    modalFooter.appendChild(editBtn);

    const deleteForm = document.createElement('form');
    deleteForm.method = 'POST';
    deleteForm.action = urlPath + `/${rowData.id}`;
    deleteForm.id = `delete${index}`;

    const hiddenMethod = document.createElement('input');
    hiddenMethod.type = 'hidden';
    hiddenMethod.name = '_method';
    hiddenMethod.value = 'DELETE';
    deleteForm.appendChild(hiddenMethod);

    const deleteBtn = document.createElement('button');
    deleteBtn.className = 'btn btn-danger';
    deleteBtn.innerHTML = document.getElementById("delete_author_button").innerHTML;
    deleteBtn.type = 'submit';
    deleteForm.appendChild(deleteBtn);

    deleteForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        // Send an Ajax request to delete the author
        $.ajax({
            url: deleteForm.action,
            type: 'POST',
            data: $(deleteForm).serialize(),
            success: function (data) {
                // Display a success message
                alert(document.getElementById("author_delete_success").innerText);

                // Remove the row from the table
                $(`#author${index}`).modal('hide');
                $(`#row${index}`).remove();

                wizard(urlPath);
            },
            error: function (jqXHR, textStatus, errorThrown, data) {
                // Display an error message
                alert(document.getElementById("author_delete_error").innerText);
            }
        });
    });

    modalFooter.appendChild(deleteForm);

    const closeBtn2 = document.createElement('button');
    closeBtn2.className = 'btn btn-primary';
    closeBtn2.innerHTML = 'Close';
    closeBtn2.setAttribute('data-bs-dismiss', 'modal');
    modalFooter.appendChild(closeBtn2);

    modalHeader.appendChild(modalTitle);
    modalHeader.appendChild(closeBtn);
    modalContent.appendChild(modalHeader);
    modalContent.appendChild(modalBody);
    modalContent.appendChild(modalFooter);
    modalDialog.appendChild(modalContent);
    modal.appendChild(modalDialog);

    return modal;
}

const makeRow = (rowData, index) => {
    const tableRow = document.createElement('tr');

    const anchor = document.createElement('a');
    anchor.setAttribute('data-bs-toggle', 'modal');
    anchor.setAttribute('data-bs-target', `#author${index}`);
    anchor.innerHTML = rowData.id;

    const tableData = document.createElement('td');
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    const modal = createModal(rowData, index, urlPath);
    tableRow.appendChild(modal);

    const firstName = document.createElement('td');
    firstName.innerHTML = rowData.firstName;
    tableRow.appendChild(firstName);

    const middleName = document.createElement('td');
    middleName.innerHTML = rowData.middleName;
    tableRow.appendChild(middleName);

    const lastName = document.createElement('td');
    lastName.innerHTML = rowData.lastName;
    tableRow.appendChild(lastName);

    return tableRow;
}

