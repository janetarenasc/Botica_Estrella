document.addEventListener('DOMContentLoaded', () => {

    const searchInput = document.getElementById('searchInput');
    const categoriaSelect = document.getElementById('categoriaSelect');
    const priceRange = document.getElementById('priceRange');
    const priceLabel = document.getElementById('priceLabel');
    const checkboxesFormato = document.querySelectorAll('.filtro-formato');
    const botonesMarca = document.querySelectorAll('.filtro-marca');
    const btnLimpiar = document.getElementById('btnLimpiar');
    const noResults = document.getElementById('noResults');
    const resultadosTexto = document.getElementById('resultadosTexto');

    const normalize = (v) => (v || '').toString().toLowerCase().trim();

    let filtrosActivos = {
        busqueda: '',
        categoria: 'todos',
        precioMax: 100,
        formatos: [],
        marcas: []
    };

    function aplicarFiltros() {

        const cards = document.querySelectorAll('.catalog-card');
        let visibles = 0;

        cards.forEach(card => {

            const nombre = normalize(card.querySelector('.product-title')?.textContent);
            const categoria = normalize(card.dataset.categoria);
            const formato = normalize(card.dataset.formato);
            const marca = normalize(card.dataset.marca);
            const precio = parseFloat(card.dataset.precio || 0);

            const pasaBusqueda = nombre.includes(filtrosActivos.busqueda);
            const pasaCategoria = filtrosActivos.categoria === 'todos' || categoria === filtrosActivos.categoria;
            const pasaPrecio = precio <= filtrosActivos.precioMax;

            const pasaFormato =
                filtrosActivos.formatos.length === 0 ||
                filtrosActivos.formatos.includes(formato);

            const pasaMarca =
                filtrosActivos.marcas.length === 0 ||
                filtrosActivos.marcas.includes(marca);

            const visible = pasaBusqueda && pasaCategoria && pasaPrecio && pasaFormato && pasaMarca;

            card.style.display = visible ? 'flex' : 'none';

            if (visible) visibles++;
        });

        if (noResults) {
            noResults.style.display = visibles === 0 ? 'flex' : 'none';
        }

        if (resultadosTexto) {
            resultadosTexto.innerHTML = `Mostrando <strong>${visibles}</strong> productos`;
        }
    }

    searchInput?.addEventListener('input', e => {
        filtrosActivos.busqueda = normalize(e.target.value);
        aplicarFiltros();
    });

    categoriaSelect?.addEventListener('change', e => {
        filtrosActivos.categoria = normalize(e.target.value);
        aplicarFiltros();
    });

    priceRange?.addEventListener('input', e => {
        filtrosActivos.precioMax = parseFloat(e.target.value);
        priceLabel.textContent = `S/. ${filtrosActivos.precioMax.toFixed(2)}`;
        aplicarFiltros();
    });

    checkboxesFormato.forEach(check => {
        check.addEventListener('change', () => {

            filtrosActivos.formatos = Array.from(checkboxesFormato)
                .filter(c => c.checked)
                .map(c => normalize(c.value));

            aplicarFiltros();
        });
    });

    botonesMarca.forEach(btn => {
        btn.addEventListener('click', () => {

            const marca = normalize(btn.dataset.marca);

            btn.classList.toggle('bg-medical-blue');
            btn.classList.toggle('text-white');
            btn.classList.toggle('bg-slate-100');
            btn.classList.toggle('text-slate-600');

            if (filtrosActivos.marcas.includes(marca)) {
                filtrosActivos.marcas = filtrosActivos.marcas.filter(m => m !== marca);
            } else {
                filtrosActivos.marcas.push(marca);
            }

            aplicarFiltros();
        });
    });

    btnLimpiar?.addEventListener('click', () => {

        filtrosActivos = {
            busqueda: '',
            categoria: 'todos',
            precioMax: 100,
            formatos: [],
            marcas: []
        };

        searchInput.value = '';
        categoriaSelect.value = 'todos';
        priceRange.value = 100;
        priceLabel.textContent = 'S/. 100.00';

        checkboxesFormato.forEach(c => c.checked = false);

        botonesMarca.forEach(btn => {
            btn.classList.remove('bg-medical-blue', 'text-white');
            btn.classList.add('bg-slate-100', 'text-slate-600');
        });

        aplicarFiltros();
    });

    aplicarFiltros();
});