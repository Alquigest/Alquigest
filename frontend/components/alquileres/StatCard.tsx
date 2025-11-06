"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ReactNode } from "react";
import { Skeleton } from "../ui/skeleton";

type StatCardProps = {
  title: string;
  value: ReactNode;
  description?: string;
  icon?: ReactNode;
};

export default function StatCard({ title, value, description, icon }: StatCardProps) {
  return (
    <>
      {(value === -99) ? <Skeleton className="h-6 w-40" /> : (
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-md md:text-md font-medium">{title}</CardTitle>
            {icon}
          </CardHeader>
          <CardContent className="flex flex-col items-center">
            <div className="text-3xl font-bold font-sans text-foreground">{value}</div>
            {description && (
              <p className="text-sm text-muted-foreground">{description}</p>
            )}
          </CardContent>
        </Card>
      )}
    </>
  );
}
