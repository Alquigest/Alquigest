"use client"

import Link from "next/link"
import PildoraUsuario from "./user-pill"
import { Bell } from "lucide-react"
import { Badge } from "@/components/ui/badge"

export default function HeaderAlquigest({ tituloPagina = "", username, toggleTheme, isDarkMode, onBellClick, showNotificationDot, onLoginClick }: any) {
  const urlLogoAlquigest = isDarkMode ? "/alquigest-white.png" : "/alquigest-dark.png"
  const gradientVar = "bg-gradient-to-l from-yellow-300 via-[var(--amarillo-alqui)] to-[var(--background)] animate-gradient-x"
  const gradientAlqui = "bg-gradient-to-l from-[var(--amarillo-alqui)] to-[var(--background)]"

  return (
    <header className={`border-b border-border ${gradientVar} shadow-lg fixed w-screen z-40`}>
      <div className="container mx-auto px-3 py-3 sm:py-5 sm:px-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <Link href="/" className="flex items-center space-x-3">
              <div className="flex items-center space-x-2 ">
                <img src={urlLogoAlquigest} className="p-2 h-10 md:h-12 contrast-70" />
                <p className="hidden md:block text-lg md:text-xl text-muted-foreground mt-1">| {tituloPagina}</p>
              </div>
            </Link>
          </div>
          <div className="flex items-center gap-4">
            {/* Bell notification icon */}
            <button
              className="relative p-2 rounded-full bg-muted hover:bg-muted-foreground transition focus:outline-none"
              aria-label="Notificaciones"
              onClick={onBellClick}
            >
              <Bell className="h-6 w-6 text-primary" />
              {showNotificationDot && (
                <span className="absolute top-1 right-1">
                  <span className="absolute flex size-3 animate-ping rounded-full bg-red-500 opacity-90"></span>
                  <span className="relative flex size-3 rounded-full bg-red-500 border-red-700 border"></span>
                </span>
              )}
            </button>
            <PildoraUsuario
              username={username}
              isDarkMode={isDarkMode}
              toggleTheme={toggleTheme}
              onLoginClick={onLoginClick}
            />
          </div>
        </div>
      </div>
    </header>
  )
}