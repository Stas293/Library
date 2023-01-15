const defaultSizeValue = 5;
const defaultStringValue = '';
let size;
let sorting = 'asc';
let search;
let sortBy = 'book_name';

function ajaxJS (url, callback) {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
        if (xhr.status === 200 && xhr.readyState === 4){
            console.log(xhr.response);
            console.log(JSON.parse(xhr.response));
            callback(JSON.parse(xhr.response));
        }
    }
    if (url.lastIndexOf('?') === url.indexOf('?')) {
        xhr.open('GET', url, true);
    } else {
        let lastIndexOf = url.lastIndexOf('?');
        xhr.open('GET', url.substring(0, lastIndexOf) + '&' + url.substring(lastIndexOf + 1), true);
    }
    xhr.send();
}

const clearTextFields = () => {
    search = document.querySelector(`#search`);
    search.value = defaultStringValue;
}

const clear = tag => {
	while (tag.firstChild) {
		tag.removeChild(tag.firstChild);
	}
}
const makeHTMLRows = (data, callback) => {
    let documentFragment = document.createDocumentFragment();
	for(let index in data){
	    documentFragment.appendChild(makeRow(data[index], index, callback));
     }
    return documentFragment;
}

const makePageNavigation = pages => {
    let documentFragment = document.createDocumentFragment();
    for(let i = 0; i < pages; i++)
        documentFragment.appendChild(makePages(i));
    return documentFragment;
}

const makePages = i => {
    let anchor = document.createElement('button');
    anchor.setAttribute('class','btn btn-light btn-sm');
    anchor.style.marginLeft='5px';
    anchor.type = 'button';
    anchor.appendChild(document.createTextNode(i+1));
    anchor.onclick = () => {
        showResults(`${urlPath}?page=${i}&limit=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
    return anchor;
}

const showResults = (url, callback) => {
	ajaxJS(url, (response) => {
        let tbody = document.getElementById('pageable-list');
        let div = document.getElementById('page-navigation');
        clear(div);
        clear(tbody);
        div.setAttribute('class','container');
        div.style.textAlign = 'center';
        let totalElements = response.elementsCount;
        let pageSize = response.limit;
        let pages = Math.ceil(totalElements/pageSize);
        tbody.appendChild(makeHTMLRows(response.content, callback));
        div.appendChild(makePageNavigation(pages));
	});
}

const setSize = () => {
    size = document.querySelector(`#size`);
    size.value = defaultSizeValue;
}

const addListeners = (url) => {
    sorting_desc.onclick = () => {
        sorting = sorting_desc.value;
        showResults(`${url}?limit=${size.value}&search=${search.value}&sorting=${sorting}&sortBy=${sortBy}`);
    }
    sorting_asc.onclick = () => {
        sorting = sorting_asc.value;
        showResults(`${url}?limit=${size.value}&search=${search.value}&sorting=${sorting}&sortBy=${sortBy}`);
    }
    size.onclick = () => {
        if (size.value < 2) {
            size.value = 2;
        } else if (size.value > 8) {
            size.value = 8;
        }
        showResults(`${url}?limit=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
    search.onkeyup = () => {
        showResults(`${url}?limit=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
}

const func = (url) => {
    showResults(`${url}?limit=${size.value}&search=${search.value}&sorting=${sorting}&sortBy=${sortBy}`);
}

const wizard = (urlPath, callback) => {
    size = document.querySelector(`#size`);
    search = document.querySelector(`#search`);
    sorting_desc = document.querySelector(`#desc`);
    sorting_asc = document.querySelector(`#asc`);
    addListeners(urlPath);
	showResults(urlPath, callback);
}

function setBookListeners (url) {
    author = document.querySelector(`#author`);
    book_name = document.querySelector(`#book_name`);
    isbn = document.querySelector(`#isbn`);
    date_publication = document.querySelector(`#date_publication`);
    author.onclick = () => {
        sortBy = 'last_name';
        func(url);
    }
    book_name.onclick = () => {
        sortBy = 'book_name';
        func(url);
    }
    isbn.onclick = () => {
        sortBy = 'ISBN';
        func(url);
    }
    date_publication.onclick = () => {
        sortBy = "book_date_publication";
        func(url);
    }
}

function setOrderListeners (url) {
    login = document.querySelector(`#login`);
    book_name = document.querySelector(`#book_name`);
    date_created = document.querySelector(`#date_created`);
    isbn = document.querySelector(`#isbn`);
    date_expire = document.querySelector(`#date_expire`);
    login.onclick = () => {
        sortBy = 'login';
        func(url);
    }
    book_name.onclick = () => {
        sortBy = 'book_name';
        func(url);
    }
    isbn.onclick = () => {
        sortBy = 'ISBN';
        func(url);
    }
    date_created.onclick = () => {
        sortBy = "date_created";
        func(url);
    }
    date_expire.onclick = () => {
        sortBy = "date_expire";
        func(url);
    }
}