// API Base URL
const API_BASE = '/api';
let currentUser = null;
let testAnswers = [];

// Initialize app
window.addEventListener('load', () => {
    checkSession();
});

// Check if user is logged in
function checkSession() {
    fetch(`${API_BASE}/auth/check-session`, {
        method: 'GET',
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        if (data.loggedIn) {
            currentUser = {
                id: data.userId,
                username: data.username
            };
            showTestPage();
        } else {
            showAuthPage();
        }
    })
    .catch(error => {
        console.error('Error checking session:', error);
        showAuthPage();
    });
}

// Switch to register form
function switchToRegister(e) {
    e.preventDefault();
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('register-form').style.display = 'block';
}

// Switch to login form
function switchToLogin(e) {
    e.preventDefault();
    document.getElementById('register-form').style.display = 'none';
    document.getElementById('login-form').style.display = 'block';
}

// Register user
function register() {
    const username = document.getElementById('register-username').value.trim();
    const email = document.getElementById('register-email').value.trim();
    const password = document.getElementById('register-password').value;
    const confirmPassword = document.getElementById('register-password-confirm').value;
    const errorDiv = document.getElementById('register-error');

    // Validation
    if (!username || !email || !password || !confirmPassword) {
        showError(errorDiv, '所有欄位都必須填寫！');
        return;
    }

    if (password !== confirmPassword) {
        showError(errorDiv, '密碼不相符！');
        return;
    }

    // API call
    const params = new URLSearchParams();
    params.append('username', username);
    params.append('email', email);
    params.append('password', password);

    fetch(`${API_BASE}/auth/register`, {
        method: 'POST',
        body: params,
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            currentUser = { id: data.userId, username: username };
            showTestPage();
        } else {
            showError(errorDiv, data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showError(errorDiv, '註冊失敗，請重試！');
    });
}

// Login user
function login() {
    const username = document.getElementById('login-username').value.trim();
    const password = document.getElementById('login-password').value;
    const errorDiv = document.getElementById('login-error');

    if (!username || !password) {
        showError(errorDiv, '用戶名和密碼不能為空！');
        return;
    }

    const params = new URLSearchParams();
    params.append('username', username);
    params.append('password', password);

    fetch(`${API_BASE}/auth/login`, {
        method: 'POST',
        body: params,
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            currentUser = { id: data.userId, username: data.username };
            showTestPage();
        } else {
            showError(errorDiv, data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showError(errorDiv, '登入失敗，請重試！');
    });
}

// Logout user
function logout() {
    fetch(`${API_BASE}/auth/logout`, {
        method: 'POST',
        credentials: 'include'
    })
    .then(() => {
        currentUser = null;
        showAuthPage();
    })
    .catch(error => console.error('Error:', error));
}

// Show auth page
function showAuthPage() {
    document.getElementById('auth-page').style.display = 'block';
    document.getElementById('test-page').style.display = 'none';
    document.getElementById('results-page').style.display = 'none';
    document.getElementById('history-page').style.display = 'none';
    
    // Reset forms
    document.getElementById('login-form').style.display = 'block';
    document.getElementById('register-form').style.display = 'none';
    document.getElementById('login-username').value = '';
    document.getElementById('login-password').value = '';
    document.getElementById('register-username').value = '';
    document.getElementById('register-email').value = '';
    document.getElementById('register-password').value = '';
    document.getElementById('register-password-confirm').value = '';
}

// Show test page
function showTestPage() {
    document.getElementById('auth-page').style.display = 'none';
    document.getElementById('test-page').style.display = 'block';
    document.getElementById('results-page').style.display = 'none';
    document.getElementById('history-page').style.display = 'none';
    
    document.getElementById('username-display').innerText = `用戶：${currentUser.username}`;
    testAnswers = [];
    loadQuestions();
}

// Load questions
function loadQuestions() {
    fetch(`${API_BASE}/questions`, {
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            renderQuestions(data.questions);
        } else {
            alert('無法加載問題！');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('加載問題失敗！');
    });
}

// Render questions
function renderQuestions(questions) {
    const container = document.getElementById('questions-container');
    container.innerHTML = '';

    questions.forEach((question, index) => {
        const questionDiv = document.createElement('div');
        questionDiv.className = 'question-group';
        
        questionDiv.innerHTML = `
            <div class="question-text">問題 ${question.questionNumber}: ${question.questionText}</div>
            <div class="option">
                <input type="radio" name="q${question.id}" value="3" onchange="updateAnswer(${index}, 3)">
                <label>A. ${question.optionA}</label>
            </div>
            <div class="option">
                <input type="radio" name="q${question.id}" value="1.5" onchange="updateAnswer(${index}, 1.5)">
                <label>B. ${question.optionB}</label>
            </div>
            <div class="option">
                <input type="radio" name="q${question.id}" value="0" onchange="updateAnswer(${index}, 0)">
                <label>C. ${question.optionC}</label>
            </div>
            <div class="option">
                <input type="radio" name="q${question.id}" value="-3" onchange="updateAnswer(${index}, -3)">
                <label>D. ${question.optionD}</label>
            </div>
        `;
        
        container.appendChild(questionDiv);
    });
}

// Update answer
function updateAnswer(index, value) {
    if (testAnswers.length <= index) {
        testAnswers[index] = value;
    } else {
        testAnswers[index] = value;
    }
}

// Submit test
function submitTest() {
    // Check if all questions are answered
    const radioButtons = document.querySelectorAll('input[type="radio"]');
    let answeredCount = 0;
    
    radioButtons.forEach(radio => {
        if (radio.checked) {
            answeredCount++;
        }
    });

    if (answeredCount < 5) {
        alert('請回答所有問題！');
        return;
    }

    // Collect answers
    const answers = [];
    document.querySelectorAll('input[type="radio"]:checked').forEach(radio => {
        answers.push(parseFloat(radio.value));
    });

    // Submit to server
    fetch(`${API_BASE}/test/submit`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ answers: answers }),
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showResultsPage(data);
        } else {
            alert(data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('提交測驗失敗！');
    });
}

// Show results page
function showResultsPage(data) {
    document.getElementById('auth-page').style.display = 'none';
    document.getElementById('test-page').style.display = 'none';
    document.getElementById('results-page').style.display = 'block';
    document.getElementById('history-page').style.display = 'none';

    document.getElementById('personality-type').innerText = data.personalityType;
    document.getElementById('personality-score').innerText = `分數：${data.score}`;
    document.getElementById('personality-description').innerText = data.description;
}

// Retake test
function retakeTest() {
    showTestPage();
}

// View history
function viewHistory() {
    fetch(`${API_BASE}/test/history`, {
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showHistoryPage(data.results);
        } else {
            alert(data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('加載歷史記錄失敗！');
    });
}

// Show history page
function showHistoryPage(results) {
    document.getElementById('auth-page').style.display = 'none';
    document.getElementById('test-page').style.display = 'none';
    document.getElementById('results-page').style.display = 'none';
    document.getElementById('history-page').style.display = 'block';

    const historyList = document.getElementById('history-list');
    historyList.innerHTML = '';

    if (results.length === 0) {
        historyList.innerHTML = '<p style="text-align: center; color: #999;">還沒有測驗記錄</p>';
        return;
    }

    results.forEach(result => {
        const date = new Date(result.testDate).toLocaleString('zh-TW');
        const historyItem = document.createElement('div');
        historyItem.className = 'history-item';
        historyItem.innerHTML = `
            <div class="history-item-title">${result.personalityType}</div>
            <div class="history-item-date">測驗時間：${date}</div>
            <div class="history-item-score">分數：${result.score}</div>
            <div class="history-item-description">${result.personalityDescription}</div>
        `;
        historyList.appendChild(historyItem);
    });
}

// Show error message
function showError(element, message) {
    element.innerText = message;
    element.classList.add('show');
    setTimeout(() => {
        element.classList.remove('show');
    }, 3000);
}