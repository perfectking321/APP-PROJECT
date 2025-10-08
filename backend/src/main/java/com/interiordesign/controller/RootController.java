package com.interiordesign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Root controller to handle requests to the root path "/"
 * Provides information about the API when accessed directly
 */
@Controller
public class RootController {
    
    /**
     * Handle root path access
     * GET /
     * 
     * @return HTML page with API information
     */
    @GetMapping("/")
    @ResponseBody
    public String root() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Interior Design API</title>
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            max-width: 800px;
                            margin: 50px auto;
                            padding: 20px;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            color: white;
                        }
                        .container {
                            background: rgba(255, 255, 255, 0.1);
                            backdrop-filter: blur(10px);
                            border-radius: 20px;
                            padding: 40px;
                            box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
                        }
                        h1 {
                            margin: 0 0 10px 0;
                            font-size: 2.5em;
                        }
                        .status {
                            display: inline-block;
                            background: #4ade80;
                            color: #000;
                            padding: 5px 15px;
                            border-radius: 20px;
                            font-weight: bold;
                            margin: 10px 0 20px 0;
                        }
                        .endpoint {
                            background: rgba(255, 255, 255, 0.2);
                            margin: 10px 0;
                            padding: 15px;
                            border-radius: 10px;
                            border-left: 4px solid #4ade80;
                        }
                        .endpoint code {
                            background: rgba(0, 0, 0, 0.3);
                            padding: 2px 8px;
                            border-radius: 4px;
                            font-family: 'Courier New', monospace;
                        }
                        .method {
                            display: inline-block;
                            padding: 3px 10px;
                            border-radius: 5px;
                            font-size: 0.85em;
                            font-weight: bold;
                            margin-right: 10px;
                        }
                        .get { background: #3b82f6; }
                        .post { background: #10b981; }
                        a {
                            color: #fbbf24;
                            text-decoration: none;
                            font-weight: bold;
                        }
                        a:hover {
                            text-decoration: underline;
                        }
                        .note {
                            background: rgba(251, 191, 36, 0.2);
                            border-left: 4px solid #fbbf24;
                            padding: 15px;
                            margin: 20px 0;
                            border-radius: 10px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>🏠 Interior Design API</h1>
                        <div class="status">✓ RUNNING</div>
                        <p>AI-powered room layout planner with rule-based furniture placement</p>
                        
                        <h2>📡 Available Endpoints</h2>
                        
                        <div class="endpoint">
                            <span class="method get">GET</span>
                            <code>/api</code> - Welcome & API information
                            <br><a href="/api" target="_blank">→ Try it</a>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method get">GET</span>
                            <code>/api/health</code> - Health check
                            <br><a href="/api/health" target="_blank">→ Try it</a>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method get">GET</span>
                            <code>/api/furniture</code> - Get all furniture items
                            <br><a href="/api/furniture" target="_blank">→ Try it</a>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method post">POST</span>
                            <code>/api/layout</code> - Generate room layout (AI)
                            <br>Body: <code>{ "length": 5.0, "width": 4.0, "budget": 2000 }</code>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method get">GET</span>
                            <code>/h2-console</code> - H2 Database Console
                            <br><a href="/h2-console" target="_blank">→ Open Console</a>
                        </div>
                        
                        <div class="note">
                            <strong>⚠️ Note:</strong> This is the backend API server only.
                            <br>For the full application UI, please run the frontend:
                            <br><code style="background: rgba(0,0,0,0.3); padding: 5px 10px; border-radius: 5px; display: inline-block; margin-top: 10px;">npm run dev</code>
                            <br>Then visit: <a href="http://localhost:3000" target="_blank">http://localhost:3000</a>
                        </div>
                    </div>
                </body>
                </html>
                """;
    }
}
