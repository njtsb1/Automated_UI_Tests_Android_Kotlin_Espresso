// Translation Localization Context Mapping
const localizations = {
    en: {
        title: "Todo",
        filter: "All Tasks",
        task1: "Build tower in Pisa",
        task2: "Finish bridge in Tacoma",
        task3: "Do the testing codelab",
        ariaToggleLight: "Toggle light theme",
        ariaToggleDark: "Toggle dark theme"
    },
    pt: {
        title: "Tarefas",
        filter: "Todas as Tarefas",
        task1: "Construir a torre em Pisa",
        task2: "Finalizar a ponte em Tacoma",
        task3: "Fazer o codelab de testes",
        ariaToggleLight: "Ativar tema claro",
        ariaToggleDark: "Ativar tema escuro"
    },
    es: {
        title: "Tareas",
        filter: "Todas las Tareas",
        task1: "Construir la torre de Pisa",
        task2: "Terminar el puente de Tacoma",
        task3: "Hacer el codelab de pruebas",
        ariaToggleLight: "Activar tema claro",
        ariaToggleDark: "Activar tema oscuro"
    }
};

// Target DOM Structural Nodes
const htmlElement = document.documentElement;
const themeToggleBtn = document.getElementById('themeToggleBtn');
const themeIcon = document.getElementById('themeIcon');
const langSelect = document.getElementById('langSelect');

const appTitle = document.getElementById('appTitle');
const filterHeading = document.getElementById('filterHeading');
const taskTexts = document.querySelectorAll('.task-text');

// Theme Switcher Controller Engine
themeToggleBtn.addEventListener('click', () => {
    const currentTheme = htmlElement.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    const currentLang = langSelect.value;
    
    htmlElement.setAttribute('data-theme', newTheme);
    
    if (newTheme === 'light') {
        themeIcon.textContent = '☀️';
        themeToggleBtn.setAttribute('aria-label', localizations[currentLang].ariaToggleDark);
    } else {
        themeIcon.textContent = '🌙';
        themeToggleBtn.setAttribute('aria-label', localizations[currentLang].ariaToggleLight);
    }
});

// Localization Application Mutation Engine
langSelect.addEventListener('change', (e) => {
    applyLocalization(e.target.value);
});

function applyLocalization(lang) {
    const data = localizations[lang];
    const currentTheme = htmlElement.getAttribute('data-theme');
    
    // Update structural text nodes
    appTitle.textContent = data.title;
    filterHeading.textContent = data.filter;
    
    taskTexts[0].textContent = data.task1;
    taskTexts[1].textContent = data.task2;
    taskTexts[2].textContent = data.task3;
    
    // Dynamic access update
    if (currentTheme === 'light') {
        themeToggleBtn.setAttribute('aria-label', data.ariaToggleDark);
    } else {
        themeToggleBtn.setAttribute('aria-label', data.ariaToggleLight);
    }
}
