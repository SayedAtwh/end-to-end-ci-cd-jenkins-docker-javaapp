document.addEventListener('DOMContentLoaded', function() {
    const quranBtn = document.getElementById('quranBtn');
    const azkarBtn = document.getElementById('azkarBtn');
    const readingModeBtn = document.getElementById('readingModeBtn');
    const themeToggle = document.getElementById('themeToggle');
    const searchInput = document.getElementById('searchInput');
    const surahList = document.getElementById('surahList');
    const surahDetail = document.getElementById('surahDetail');
    const azkarList = document.getElementById('azkarList');
    const incrementBtn = document.getElementById('incrementBtn');
    const resetBtn = document.getElementById('resetBtn');
    const counter = document.getElementById('counter');
    const increaseFont = document.getElementById('increaseFont');
    const decreaseFont = document.getElementById('decreaseFont');
    const readingContent = document.getElementById('readingContent');

    let currentSurahs = [];
    let currentAzkar = [];
    let tasbeehCount = 0;
    let fontSize = 1.5;

    // Theme toggle
    themeToggle.addEventListener('click', function() {
        document.body.classList.toggle('dark');
    });

    // Navigation
    quranBtn.addEventListener('click', showQuran);
    azkarBtn.addEventListener('click', showAzkar);
    readingModeBtn.addEventListener('click', showReadingMode);

    function showSection(sectionId) {
        document.querySelectorAll('.section').forEach(section => section.classList.add('hidden'));
        document.getElementById(sectionId).classList.remove('hidden');
    }

    function showQuran() {
        showSection('quranSection');
        loadSurahs();
    }

    function showAzkar() {
        showSection('azkarSection');
        loadAzkar();
    }

    function showReadingMode() {
        showSection('readingModeSection');
        loadReadingContent();
    }

    // Load Quran data
    async function loadSurahs() {
        try {
            const response = await fetch('/api/quran/surahs');
            currentSurahs = await response.json();
            displaySurahs(currentSurahs);
        } catch (error) {
            console.error('Error loading Surahs:', error);
        }
    }

    function displaySurahs(surahs) {
        surahList.innerHTML = '';
        surahs.forEach(surah => {
            const surahItem = document.createElement('div');
            surahItem.className = 'surah-item';
            surahItem.textContent = `${surah.number}. ${surah.name} (${surah.englishName})`;
            surahItem.addEventListener('click', () => loadSurahDetail(surah.number));
            surahList.appendChild(surahItem);
        });
    }

    async function loadSurahDetail(number) {
        try {
            const response = await fetch(`/api/quran/surah/${number}`);
            const surah = await response.json();
            displaySurahDetail(surah);
        } catch (error) {
            console.error('Error loading Surah detail:', error);
        }
    }

    function displaySurahDetail(surah) {
        surahDetail.innerHTML = `<h3>${surah.name} (${surah.englishName})</h3>`;
        surah.ayahs.forEach((ayah, index) => {
            const ayahDiv = document.createElement('div');
            ayahDiv.className = 'ayah';
            ayahDiv.textContent = ayah;
            surahDetail.appendChild(ayahDiv);
        });
    }

    // Search
    searchInput.addEventListener('input', function() {
        const query = this.value.toLowerCase();
        const filtered = currentSurahs.filter(surah =>
            surah.name.toLowerCase().includes(query) ||
            surah.englishName.toLowerCase().includes(query)
        );
        displaySurahs(filtered);
    });

    // Load Azkar data
    async function loadAzkar() {
        try {
            const response = await fetch('/api/azkar');
            currentAzkar = await response.json();
            displayAzkar(currentAzkar);
        } catch (error) {
            console.error('Error loading Azkar:', error);
        }
    }

    function displayAzkar(azkar) {
        azkarList.innerHTML = '';
        azkar.forEach(category => {
            const categoryDiv = document.createElement('div');
            categoryDiv.className = 'azkar-category';
            categoryDiv.innerHTML = `<h3>${category.category}</h3>`;
            category.azkarList.forEach(azkarText => {
                const azkarItem = document.createElement('div');
                azkarItem.className = 'azkar-item';
                azkarItem.textContent = azkarText;
                categoryDiv.appendChild(azkarItem);
            });
            azkarList.appendChild(categoryDiv);
        });
    }

    // Tasbeeh counter
    incrementBtn.addEventListener('click', function() {
        tasbeehCount++;
        counter.textContent = tasbeehCount;
    });

    resetBtn.addEventListener('click', function() {
        tasbeehCount = 0;
        counter.textContent = tasbeehCount;
    });

    // Reading mode
    increaseFont.addEventListener('click', function() {
        fontSize += 0.1;
        readingContent.style.fontSize = fontSize + 'rem';
    });

    decreaseFont.addEventListener('click', function() {
        fontSize -= 0.1;
        readingContent.style.fontSize = fontSize + 'rem';
    });

    function loadReadingContent() {
        // For demo, load first Surah
        loadSurahDetail(1);
        setTimeout(() => {
            readingContent.innerHTML = surahDetail.innerHTML;
        }, 100);
    }

    // Initialize with Quran
    showQuran();
});