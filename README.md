<div align="center">

<br />

```
 ██████╗ ███╗   ██╗██╗███████╗██╗███████╗██████╗     ███████╗ █████╗ ██╗      █████╗ ██╗  ██╗
██╔═══██╗████╗  ██║██║██╔════╝██║██╔════╝██╔══██╗    ██╔════╝██╔══██╗██║     ██╔══██╗██║  ██║
██║   ██║██╔██╗ ██║██║█████╗  ██║█████╗  ██║  ██║    ███████╗███████║██║     ███████║███████║
██║   ██║██║╚██╗██║██║██╔══╝  ██║██╔══╝  ██║  ██║    ╚════██║██╔══██║██║     ██╔══██║██╔══██║
╚██████╔╝██║ ╚████║██║██║     ██║███████╗██████╔╝    ███████║██║  ██║███████╗██║  ██║██║  ██║
 ╚═════╝ ╚═╝  ╚═══╝╚═╝╚═╝     ╚═╝╚══════╝╚═════╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝
```

<br />

**A minimal, dark-first prayer time dashboard built for clarity and calm.**

[![Live Demo](https://img.shields.io/badge/live_demo-salah.aqib.cloud-3b82f6?style=for-the-badge&logoColor=white)](https://salah.aqib.cloud)
![Status](https://img.shields.io/badge/status-active-22c55e?style=for-the-badge)
![License](https://img.shields.io/badge/license-MIT-64748b?style=for-the-badge)
![API](https://img.shields.io/badge/api-London_Prayer_Times-3b82f6?style=for-the-badge)

<br />

> *"Indeed, prayer has been decreed upon the believers a decree of specified times."*
> — Surah An-Nisa, 4:103

<br />

</div>

---

## Overview

Unified Salah App is a lightweight, single-purpose web application that surfaces daily Islamic prayer times in a clean, dark UI. No bloat, no ads, no account required. Just the five daily prayers, a live countdown, and a daily Quranic ayah rendered with care.

The app pulls structured prayer time data from the [London Prayer Times API](https://londonprayertimes.com/api/) and presents it through a tile-based interface designed to be readable at a glance, at any hour.

Live at **[salah.aqib.cloud](https://salah.aqib.cloud)**

---

## Features

### Core

- **Dark tile layout** — Five prayer time cards arranged in a minimal grid. Each tile surfaces the prayer name, time, and active state. Low visual noise, high legibility.
- **Live countdown** — A real-time timer counts down to the next Salah, updating every second.
- **Ayah of the day** — A rotating Quranic verse rendered prominently with a blue accent treatment. Sourced and presented with full surah and ayah reference.
- **Blue accent system** — A single `#3b82f6` accent anchors the visual hierarchy throughout: active prayer highlights, the countdown ring, the ayah card border, and interactive states.
- **Responsive grid** — Tiles reflow gracefully across mobile, tablet, and desktop breakpoints.

### Upcoming

- **Date picker integration** — Query prayer times for any future date directly from the UI. The API already supports this; the frontend layer is in progress.
- **Location awareness** — Auto-detect user location and resolve times without manual city entry.
- **Hijri calendar overlay** — Display the current Islamic date alongside the Gregorian date.
- **Notification support** — Browser push notifications for each prayer time, with Adhan audio toggle.
- **PWA packaging** — Installable as a home screen app with offline tile caching for previously fetched dates.

---

## API

Prayer time data is sourced from the **London Prayer Times API** by londonprayertimes.com.

### Endpoint

```
GET https://londonprayertimes.com/api/times/
```

### Query Parameters

| Parameter | Type   | Required | Description                          |
|-----------|--------|----------|--------------------------------------|
| `key`     | string | Yes      | Your API key                         |
| `date`    | string | No       | Target date in `YYYY-MM-DD` format. Defaults to today. |
| `year`    | string | No       | Return all times for a given year    |
| `month`   | string | No       | Return all times for a given month   |
| `24hours` | bool   | No       | Return times in 24h format           |

### Example Request

```bash
curl "https://londonprayertimes.com/api/times/?key=YOUR_API_KEY&date=2026-03-27&24hours=true"
```

### Example Response

```json
{
  "times": {
    "fajr":    "05:12",
    "sunrise": "06:30",
    "zuhr":    "12:27",
    "asr":     "15:44",
    "magrib":  "18:19",
    "isha":    "19:45"
  }
}
```

> **Note:** API keys are redacted from all source code and environment configs. The key in the example above is a placeholder.

### Getting an API Key

Register for a free API key at [londonprayertimes.com/api/](https://londonprayertimes.com/api/). Keys are issued per project and rate-limited per the provider's fair use policy.

---

## Getting Started

### Prerequisites

- Node.js 18+
- A valid London Prayer Times API key

### Installation

```bash
git clone https://github.com/aqib/unified-salah-app.git
cd unified-salah-app
npm install
```

### Environment Variables

Create a `.env` file in the project root:

```env
VITE_PRAYER_API_KEY=your_api_key_here
```

> Never commit your `.env` file. It is listed in `.gitignore` by default.

### Development

```bash
npm run dev
```

### Production Build

```bash
npm run build
npm run preview
```

---

## Project Structure

```
unified-salah-app/
├── src/
│   ├── components/
│   │   ├── PrayerTile.jsx       # Individual prayer card
│   │   ├── Countdown.jsx        # Next prayer countdown
│   │   ├── AyahCard.jsx         # Daily Quranic verse display
│   │   └── DateSelector.jsx     # Future date picker (WIP)
│   ├── hooks/
│   │   ├── usePrayerTimes.js    # API fetch + caching logic
│   │   └── useCountdown.js      # Real-time countdown logic
│   ├── lib/
│   │   └── api.js               # Typed API client
│   └── App.jsx
├── .env.example
├── .gitignore
└── vite.config.js
```

---

## Design System

| Token           | Value       | Usage                              |
|-----------------|-------------|------------------------------------|
| `--blue-accent` | `#3b82f6`   | Active tile, countdown, ayah card  |
| `--bg-base`     | `#0a0a0a`   | Page background                    |
| `--bg-tile`     | `#111111`   | Prayer tile surface                |
| `--bg-tile-hover` | `#1a1a1a` | Hover state                        |
| `--text-primary` | `#f1f5f9`  | Primary labels                     |
| `--text-muted`  | `#64748b`   | Inactive prayer names, metadata    |

Typography is set in a geometric monospace for times, paired with a humanist sans for all other text. The combination keeps the UI feeling precise without being cold.

---

## Roadmap

- [x] Static prayer time tiles
- [x] Live countdown to next Salah
- [x] Ayah of the day with blue accent card
- [x] 24h / 12h format toggle
- [ ] Future date query via date picker
- [ ] Hijri date display
- [ ] Location-based auto-resolution
- [ ] Browser push notifications
- [ ] Adhan audio on prayer time
- [ ] PWA manifest + service worker

---

## Contributing

Pull requests are open. If you spot a bug, have a feature suggestion, or want to contribute to any of the roadmap items above, open an issue first so we can align before you start building.

```bash
git checkout -b feature/your-feature-name
git commit -m "feat: describe your change"
git push origin feature/your-feature-name
```

---

## License

MIT. Do what you want with it. Attribution appreciated but not required.

---

<div align="center">

Built with focus by **[Aqib](https://aqib.cloud)**

[salah.aqib.cloud](https://salah.aqib.cloud)

</div>
