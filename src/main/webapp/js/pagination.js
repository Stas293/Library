let defaultSizeValue = 5;
let defaultStringValue = '';
let size, sorting = 'ASC', search, sortBy = "", sorting_desc, sorting_asc;

function ajaxJS(url, callback) {
    console.log(url);
    let indexFirst = url.indexOf("?");
    let lastIndexOf = url.lastIndexOf("?");
    if (indexFirst !== lastIndexOf) {
        url = url.substring(0, lastIndexOf) + '&' + url.substring(lastIndexOf + 1);
    }
    console.log(url);
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            callback(data);
        },
        error: function (xhr, status, error) {
            console.log(xhr);
            console.log(status);
            console.log(error);
        }
    });
}

const clearTextFields = function () {
    let querySelector = document.querySelector("#search");
    querySelector.setAttribute("value", "");
};
const clear = function (tag) {
    while (tag.firstChild) {
        tag.removeChild(tag.firstChild);
    }
};
const makeHTMLRows = function (data, callback) {
    const documentFragment = document.createDocumentFragment();
    data.forEach(function (rowData, index) {
        documentFragment.appendChild(makeRow(rowData, index, callback));
    });
    return documentFragment;
};
const showResults = function (url, callback) {
    ajaxJS(url, function (response) {
        const tbody = document.getElementById('pageable-list');
        const div = document.getElementById('page-navigation');
        clear(div);
        clear(tbody);
        div.setAttribute('class', 'container');
        div.style.textAlign = 'center';
        const pages = response.totalPages;
        tbody.appendChild(makeHTMLRows(response.content, callback));
        div.appendChild(makePageNavigation(pages));
    });
};
const makePages = function (i) {
    const anchor = document.createElement('button');
    anchor.setAttribute('class', 'btn btn-light btn-sm');
    anchor.style.marginLeft = '5px';
    anchor.type = 'button';
    anchor.appendChild(document.createTextNode(i + 1));
    anchor.onclick = function () {
        let pathToRequest = `${urlPath}?page=${i}&limit=${size.value}&search=${search.value}&sorting=${sorting}`;
        showResults(pathToRequest);
    };
    return anchor;
};
const makePageNavigation = function (pages) {
    const documentFragment = document.createDocumentFragment();
    for (let i = 0; i < pages; i++)
        documentFragment.appendChild(makePages(i));
    return documentFragment;
};
const addListeners = function (url) {
    sorting_desc.onclick = function () {
        sorting = sorting_desc.value;
        let pathToRequest = `${url}?limit=${size.value}&search=${search.value}&sorting=${sorting}&sortBy=${sortBy}`;
        showResults(pathToRequest);
    };
    sorting_asc.onclick = function () {
        sorting = sorting_asc.value;
        let pathToRequest = `${url}?limit=${size.value}&search=${search.value}&sorting=${sorting}&sortBy=${sortBy}`;
        showResults(pathToRequest);
    };
    size.onclick = function () {
        if (size.value < 2) {
            size.value = 2;
        } else if (size.value > 8) {
            size.value = 8;
        }
        let pathToRequest = `${url}?limit=${size.value}&search=${search.value}&sorting=${sorting}&sortBy=${sortBy}`;
        showResults(pathToRequest);
    };
    search.onkeyup = function () {
        let pathToRequest = `${url}?limit=${size.value}&search=${search.value}&sorting=${sorting}`;
        showResults(pathToRequest);
    };
};
const func = function (url) {
    let pathToRequest = `${url}?limit=${size.value}&search=${search.value}&sorting=${sorting}&sortBy=${sortBy}`;
    showResults(pathToRequest);
};
const wizard = function (urlPath, callback) {
    size = document.querySelector("#size");
    search = document.querySelector("#search");
    sorting_desc = document.querySelector("#desc");
    sorting_asc = document.querySelector("#asc");
    sortBy = "";
    addListeners(urlPath);
    func(urlPath);
};

const setSize = () => {
    size = document.querySelector(`#size`);
    size.value = defaultSizeValue;
}

function setBookListeners(url) {
    const book_title = document.querySelector("#book_title");
    const isbn = document.querySelector("#isbn");
    const date_publication = document.querySelector("#date_publication");
    const book_authors = document.querySelector("#book_authors");
    book_title.onclick = function () {
        sortBy = "title";
        func(url);
    };
    isbn.onclick = function () {
        sortBy = "isbn";
        func(url);
    };
    date_publication.onclick = function () {
        sortBy = "date_publication";
        func(url);
    };
    book_authors.onclick = function () {
        sortBy = "last_name";
        func(url);
    };
    sortBy = "title";
}

function setUsersListeners(urlPath) {
    const login = document.querySelector("#login");
    const first_name = document.querySelector("#first_name");
    const last_name = document.querySelector("#last_name");
    const phone = document.querySelector("#phone");

    login.onclick = function () {
        sortBy = "login";
        func(urlPath);
    }
    first_name.onclick = function () {
        sortBy = "first_name";
        func(urlPath);
    }
    last_name.onclick = function () {
        sortBy = "last_name";
        func(urlPath);
    }
    phone.onclick = function () {
        sortBy = "phone";
        func(urlPath);
    }
    sortBy = "login";
}

function setAuthorsListeners(urlPath) {
    const author_id = document.querySelector("#author_id");
    const first_name = document.querySelector("#author_first_name");
    const last_name = document.querySelector("#author_last_name");
    const middle_name = document.querySelector("#author_middle_name");

    author_id.onclick = function () {
        sortBy = "id";
        func(urlPath);
    }
    first_name.onclick = function () {
        sortBy = "first_name";
        func(urlPath);
    }
    last_name.onclick = function () {
        sortBy = "last_name";
        func(urlPath);
    }
    middle_name.onclick = function () {
        sortBy = "middle_name";
        func(urlPath);
    }
    sortBy = "id";
}

function setHistoryListeners(url) {
    const book_title = document.querySelector("#book_name");
    const date_return = document.querySelector("#date_returned");
    const date_created = document.querySelector("#date_created");
    book_title.onclick = function () {
        sortBy = "book_title";
        func(url);
    };
    date_return.onclick = function () {
        sortBy = "date_returned";
        func(url);
    };
    date_created.onclick = function () {
        sortBy = "date_created";
        func(url);
    };
    sortBy = "date_created";
}
