import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { BancoService } from '../../services/banco.service';
import { AuthService } from '../../services/auth.service';
import { Transaccion } from '../../models/banco.model';

@Component({
  selector: 'app-transacciones',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './transacciones.component.html'
})
export class TransaccionesComponent implements OnInit, OnDestroy {
  transacciones: Transaccion[] = [];
  transaccionSeleccionada: Transaccion | null = null;
  terminoBusqueda = '';
  loading = false;
  errorInfo = '';
  paginaActual = 0;
  totalPaginas = 1;
  private buscadorSubject = new Subject<string>();
  private subscripcion?: Subscription;

  constructor(
    private bancoService: BancoService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.subscripcion = this.buscadorSubject.pipe(
      debounceTime(350),
      distinctUntilChanged()
    ).subscribe(texto => this.cargarTransacciones(texto, 0));
    this.cargarTransacciones('', 0);
  }

  onInputCambio(valor: string): void {
    this.buscadorSubject.next(valor);
  }

  cargarTransacciones(termino: string, pagina: number): void {
    this.loading = true;
    this.errorInfo = '';
    this.bancoService.buscarTransaccionesPaginadas(termino, pagina, 10).subscribe({
      next: (res) => {
        this.transacciones = res.content;
        this.totalPaginas = res.totalPages;
        this.paginaActual = res.number;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.errorInfo = 'Error al cargar historial: ' + (err.error?.detail || err.message);
      }
    });
  }

  anterior(): void {
    if (this.paginaActual > 0) {
      this.cargarTransacciones(this.terminoBusqueda, this.paginaActual - 1);
    }
  }

  siguiente(): void {
    if (this.paginaActual < this.totalPaginas - 1) {
      this.cargarTransacciones(this.terminoBusqueda, this.paginaActual + 1);
    }
  }

  restablecer(): void {
    this.terminoBusqueda = '';
    this.onInputCambio('');
  }

  inyectarPayloadSql(): void {
    this.terminoBusqueda = "' OR '1'='1";
    this.onInputCambio(this.terminoBusqueda);
  }

  verComprobante(t: Transaccion): void {
    this.transaccionSeleccionada = t;
  }

  cerrarComprobante(): void {
    this.transaccionSeleccionada = null;
  }

  ngOnDestroy(): void {
    this.subscripcion?.unsubscribe();
  }
}
